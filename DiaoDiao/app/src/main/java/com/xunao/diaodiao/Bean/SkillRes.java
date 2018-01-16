package com.xunao.diaodiao.Bean;

import java.util.List;

/**
 * Description:
 * Created by GUZHENFU on 2017/11/13 16:52.
 */

public class SkillRes {
    private List<SkillBean> skills;
    private List<SkillBean> tags;

    public List<SkillBean> getTags() {
        return tags;
    }

    public void setTags(List<SkillBean> tags) {
        this.tags = tags;
    }

    public List<SkillBean> getSkills() {
        return skills;
    }

    public void setSkills(List<SkillBean> skills) {
        this.skills = skills;
    }

    public static class SkillBean{
        private String id;
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
