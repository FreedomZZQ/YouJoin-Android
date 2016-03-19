package me.zq.youjoin.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import me.zq.youjoin.DataPresenter;
import me.zq.youjoin.R;
import me.zq.youjoin.YouJoinApplication;
import me.zq.youjoin.model.PluginInfo;
import me.zq.youjoin.network.NetworkManager;
import me.zq.youjoin.plugin.DownloadProgressListener;
import me.zq.youjoin.plugin.FileDownloader;
import me.zq.youjoin.plugin.ZipExtractorTask;
import me.zq.youjoin.utils.LogUtils;
import me.zq.youjoin.utils.StringUtils;

public class PluginDownloadActivity extends BaseActivity
        implements DataPresenter.GetPluginList {

    List<PluginInfo.PlugEntity> dataList = new ArrayList<>();
    List<Integer> progressList = new ArrayList<>();
    Map<Integer, Boolean> downloadStatus = new HashMap<>();

    PluginDownloadAdapter adapter;
    @Bind(R.id.download_plugin_list)
    ListView downloadPluginList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plugin_download);
        ButterKnife.bind(this);

        DataPresenter.requestPluginList(YouJoinApplication.getCurrUser().getId(),
                PluginDownloadActivity.this);

        adapter = new PluginDownloadAdapter(PluginDownloadActivity.this, dataList);
        downloadPluginList.setAdapter(adapter);
        downloadPluginList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (downloadStatus.containsKey(position) && !downloadStatus.get(position)) {
                    downloadStatus.put(position, true);
                    adapter.notifyDataSetChanged();

                    // 获取下载路径
                    String path = dataList.get(position).getPlugin();
                    // 获取SDCard是否存在,并且是否具有读写权限
                    if (Environment.getExternalStorageState().equals(
                            Environment.MEDIA_MOUNTED)) {
                        // 获取SDCard根目录文件
                        String savePath = Environment.getExternalStorageDirectory() + "/YouJoinFiles/download/";
                        File saveDir = new File(savePath);
                        download(StringUtils.getPicUrlList(path).get(0), saveDir);
                    }
                    // 当SDCard不存在时
                    else {
                        Toast.makeText(PluginDownloadActivity.this, R.string.download_sdcarderr,
                                Toast.LENGTH_LONG).show();
                    }

                } else {

                    if (downloadStatus.containsKey(position)){
                        adapter.notifyDataSetChanged();
                        // 停止下载
                        exit();
                    }
                    downloadStatus.put(position, false);

                }

            }
        });

    }


    @Override
    public void onGetPluginList(PluginInfo info) {
        if (info.getResult().equals(NetworkManager.FAILURE)) {
            return;
        }

        dataList.clear();

        for (PluginInfo.PlugEntity entity : info.getPlug()) {
            dataList.add(entity);
        }

        adapter.notifyDataSetChanged();

    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, PluginDownloadActivity.class);
        context.startActivity(intent);
    }

    private DownloadTask task;
    private Thread myThread;
    private FileDownloader loader;// 文件下载器（下载线程的容器）
    private int totalSize = 100;
    private int currentSize = 0;
    // 正在下载实时数据传输Message标志
    private static final int PROCESSING = 1;
    // 下载失败时的Message标志
    private static final int FAILURE = -1;
    private UIHander handler = new UIHander();

    /**
     * 系统会自动调用的回调方法，用于处理消息事件 Message一般会包含消息的标志和消息的内容以及消息的处理器Handler
     */
    private final class UIHander extends Handler {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                // 下载时
                case PROCESSING:
                    // 从消息中获取已经下载的数据长度
                    currentSize = msg.getData().getInt("size");
                    adapter.notifyDataSetChanged();
                    break;
                // 下载失败时
                case -1:
                    // 提示用户下载失败
                    Toast.makeText(PluginDownloadActivity.this, R.string.download_failure,
                            Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }

    /**
     * 退出下载
     */
    public void exit() {
        // 如果有下载对象时，退出下载
        if (task != null)
            task.exit();
    }

    /**
     * 下载资源，声明下载执行者并开辟线程开始下载
     *
     * @param path
     *            下载的路径
     * @param saveFile
     *            保存文件
     */
    private void download(String path, File saveFile) {
        //if (task == null)
        // 实例化下载任务 7
        task = new DownloadTask(path, saveFile);
        // 开始下载
        myThread = new Thread(task);
        myThread.start();
    }

    private final class DownloadTask implements Runnable {

        private String path;// 下载路径
        private File saveDir;// 下载到保存到的文件

        /**
         * 构造方法，实现变量初始化
         *
         * @param path
         * @param saveDir
         */
        public DownloadTask(String path, File saveDir) {
            this.path = path;
            this.saveDir = saveDir;
        }

        public void exit() {
            // 如果下载器存在的话则退出下载
            if (loader != null)
                loader.exit();
        }

        // 开始下载，并设置下载的监听器
        DownloadProgressListener downloadProgressListener = new DownloadProgressListener() {

            @Override
            public void onDownloadSize(int size) {
                Message msg = new Message();
                msg.what = PROCESSING;// 设置ID为1
                msg.getData().putInt("size", size);// 把文件下载的size设置进Message对象
                handler.sendMessage(msg);
            }
        };

        // 下载线程的执行方法，会被系统自动调用
        @Override
        public void run() {

            // 初始化下载
            try {
                loader = new FileDownloader(PluginDownloadActivity.this, path, saveDir, 1);
                totalSize = loader.getFileSize();
                loader.download(downloadProgressListener);
            } catch (Exception e) {
                // 下载失败时向消息队列发送消息
                e.printStackTrace();
                handler.sendMessage(handler.obtainMessage(FAILURE));

            }
        }

    }

    public void doZipExtractorWork(){
        ZipExtractorTask task = new ZipExtractorTask(
                Environment.getExternalStorageDirectory().getPath() + "/pintu.zip",
                Environment.getExternalStorageDirectory().getPath() + "/YouJoinFiles/", this, true);
        task.execute();
    }

    public class PluginDownloadAdapter extends BaseAdapter {

        public static final String TAG = "YouJoin";

        private List<PluginInfo.PlugEntity> dataList;
        private LayoutInflater inflater;
        private Context context;

        public PluginDownloadAdapter(Context context, List<PluginInfo.PlugEntity> dataList){
            this.dataList = dataList;
            this.inflater = LayoutInflater.from(context);
            this.context = context;
        }

        @Override
        public int getCount(){
            return dataList.size();
        }

        @Override
        public Object getItem(int position){
            return dataList.get(position);
        }

        @Override
        public long getItemId(int position){
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent){
            final ViewHolder holder;
            if(convertView == null){

                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.plugin_download_item, null);
                holder.picture = (CircleImageView) convertView.findViewById(R.id.plugin_picture);
                holder.name = (TextView) convertView.findViewById(R.id.plugin_name);
                holder.version = (TextView) convertView.findViewById(R.id.plugin_version);
                holder.introduce = (TextView) convertView.findViewById(R.id.plugin_introduce);
                //holder.progressLayout = (ProgressLayout) convertView.findViewById(R.id.progressLayout);
                holder.downloadAction = (ImageView) convertView.findViewById(R.id.download_action);

                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }

            if(dataList.get(position).getPicture() != null){
                Picasso.with(context)
                        .load(StringUtils.getPicUrlList(dataList.get(position).getPicture()).get(0))
                        .resize(300, 300)
                        .centerCrop()
                        .into(holder.picture);
            }else {
                LogUtils.e(TAG, "MessageListAdapter get pic failure");
            }

            holder.name.setText(dataList.get(position).getPlugin_name());
            holder.version.setText(dataList.get(position).getVision());
            holder.introduce.setText(dataList.get(position).getIntroduce());

            //holder.progressLayout.setCurrentProgress(currentSize / totalSize * 100);

//            if (!holder.progressLayout.isPlaying()) {
//
//                holder.progressLayout.start();
//                holder.downloadAction.setBackgroundResource(R.drawable.pause);
//                notifyDataSetChanged();
//
//            } else {
//                holder.progressLayout.stop();
//                holder.downloadAction.setBackgroundResource(R.drawable.play);
//                notifyDataSetChanged();
//            }



//            holder.progressLayout.setProgressLayoutListener(new ProgressLayoutListener() {
//                @Override public void onProgressCompleted() {
//                    holder.downloadAction.setBackgroundResource(R.drawable.play);
//                    Toast.makeText(PluginDownloadActivity.this, R.string.download_finished,
//                            Toast.LENGTH_LONG).show();
//                    doZipExtractorWork();
//                }
//
//                @Override public void onProgressChanged(int seconds) {
//                    //holder.textViewDuration.setText(calculateSongDuration(seconds));
//                }
//            });

            return convertView;
        }

        public final class ViewHolder{
            public CircleImageView picture;
            public TextView name;
            public TextView version;
            public TextView introduce;
//            public ProgressLayout progressLayout;
            public ImageView downloadAction;
        }
    }

}
