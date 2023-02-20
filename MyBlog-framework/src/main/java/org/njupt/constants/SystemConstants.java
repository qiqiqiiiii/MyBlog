package org.njupt.constants;

public class SystemConstants {
    /**
     * 文章状态：草稿
     */
    public static final int ARTICLE_STATUS_DRAFT=1;

    /**
     *文章状态：已发布
     */
    public static final int ARTICLE_STATUS_NORMAL=0;

    /**
     * 热门文章排名的页数
     */
    public static final int HOTARTICLE_PAGE_NUM=1;

    /**
     * 热门文章排名的数量
     */
    public static final int HOTARTICLE_NUM=10;

    /**
     * 文章种类的状态：正常
     */
    public static final String CATEGORY_STATUS_NORMAL="0";

    /**
     * 文章种类的状态：禁用
     */
    public static final String CATEGORY_STATUS_DISABLE="1";

    /**
     * 友链审核通过
     */
    public static final String LINK_STATUS_APPROVED="0";

    /**
     * 友链审核未通过
     */
    public static final String LINK_STATUS_FAILED="1";

    /**
     * 友链未审核
     */
    public static final String LINK_STATUS_PROCESS="2";

    /**
     * 评论表，根评论
     */
    public static final Long COMMENT_ROOT=-1L;

    /**
     * 文章评论
     */
    public static final String ARTICLE_COMMENT="0";
    /**
     * 友链评论
     */
    public static final String LINK_COMMENT="1";

    /**
     * 菜单类型：M目录
     */
    public static final String MENU_TYPE_MENU="M";

    /**
     * 菜单类型：C菜单
     */
    public static final String MENU_TYPE_CATALOGUE="C";
    /**
     * 菜单类型：F按钮
     */
    public static final String MENU_TYPE_BUTTON="F";

    /**
     * 菜单状态
     */
    public static final String MENU_STATUS_NORMAL="0";

    /**
     * 父菜单
     */
    public static final Long MENU_PARENT=0L;

    /**
     * 用户类型 普通用户
     */
    public static final String ORDINARY_USER="0";

    /**
     * 用户类型 管理员
     */
    public static final String ADMIN="1";

    /**
     * 角色表，超级管理员
     */
    public static final Long ROLE_SUPER_ADMIN=1L;

    /**
     * 角色表，角色状态
     */
    public static final String ROLE_STATUS_NORMAL="0";
}
