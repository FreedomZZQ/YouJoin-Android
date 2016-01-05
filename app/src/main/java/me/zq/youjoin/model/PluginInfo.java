package me.zq.youjoin.model;

import java.util.List;

/**
 * YouJoin-Android
 * Created by ZQ on 2016/1/5.
 */
public class PluginInfo {
    /**
     * result : success
     * plug : [{"plugin_id":"16","plugin_name":"3D拼图游戏","picture":"http://www.tekbroaden.com/youjoin-server/admin/upload/admin/20160105013216_2b076059252dd42aafe1fd8d043b5bb5c8eab8de.jpg;","vision":"v1.0","plugin":"http://www.tekbroaden.com/youjoin-server/admin/plugin/admin/v1.0_pintu.zip;","issue_time":"2016-01-05","introduce":"3D拼图游戏"}]
     */

    private String result;
    /**
     * plugin_id : 16
     * plugin_name : 3D拼图游戏
     * picture : http://www.tekbroaden.com/youjoin-server/admin/upload/admin/20160105013216_2b076059252dd42aafe1fd8d043b5bb5c8eab8de.jpg;
     * vision : v1.0
     * plugin : http://www.tekbroaden.com/youjoin-server/admin/plugin/admin/v1.0_pintu.zip;
     * issue_time : 2016-01-05
     * introduce : 3D拼图游戏
     */

    private List<PlugEntity> plug;

    public void setResult(String result) {
        this.result = result;
    }

    public void setPlug(List<PlugEntity> plug) {
        this.plug = plug;
    }

    public String getResult() {
        return result;
    }

    public List<PlugEntity> getPlug() {
        return plug;
    }

    public static class PlugEntity {
        private String plugin_id;
        private String plugin_name;
        private String picture;
        private String vision;
        private String plugin;
        private String issue_time;
        private String introduce;

        public void setPlugin_id(String plugin_id) {
            this.plugin_id = plugin_id;
        }

        public void setPlugin_name(String plugin_name) {
            this.plugin_name = plugin_name;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public void setVision(String vision) {
            this.vision = vision;
        }

        public void setPlugin(String plugin) {
            this.plugin = plugin;
        }

        public void setIssue_time(String issue_time) {
            this.issue_time = issue_time;
        }

        public void setIntroduce(String introduce) {
            this.introduce = introduce;
        }

        public String getPlugin_id() {
            return plugin_id;
        }

        public String getPlugin_name() {
            return plugin_name;
        }

        public String getPicture() {
            return picture;
        }

        public String getVision() {
            return vision;
        }

        public String getPlugin() {
            return plugin;
        }

        public String getIssue_time() {
            return issue_time;
        }

        public String getIntroduce() {
            return introduce;
        }
    }
}
