package info.xiaomo.server.system.user;

import info.xiaomo.server.db.DbData;
import info.xiaomo.server.db.DbDataType;
import info.xiaomo.server.entify.User;
import info.xiaomo.server.event.EventType;
import info.xiaomo.server.event.EventUtil;
import info.xiaomo.server.server.Session;
import info.xiaomo.server.server.SessionManager;
import info.xiaomo.server.system.user.msg.ResLoginMessage;
import info.xiaomo.server.util.IDUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 把今天最好的表现当作明天最新的起点．．～
 * いま 最高の表現 として 明日最新の始発．．～
 * Today the best performance  as tomorrow newest starter!
 * Created by IntelliJ IDEA.
 * <p>
 * author: xiaomo
 * github: https://github.com/xiaomoinfo
 * email : xiaomo@xiaomo.info
 * QQ    : 83387856
 * Date  : 2017/7/13 15:02
 * desc  :
 * Copyright(©) 2017 by xiaomo.
 */
public class UserManager {
    public static final Logger LOGGER = LoggerFactory.getLogger(UserManager.class);
    private static UserManager ourInstance = new UserManager();

    public static UserManager getInstance() {
        return ourInstance;
    }

    private UserManager() {
    }

    public void login(Session session, String loginName) {
        User user = DbData.getUser(loginName);
        if (user == null) {
            // 新建用户
            user = createUser(loginName);
            if (user == null) {
                LOGGER.error("用户创建失败:{},{},{}", loginName);
                session.close();
                return;
            }
        }

        session.setUser(user); // 注册账户
        SessionManager.getInstance().register(user.getId(), session);// 注册session

        ResLoginMessage res = new ResLoginMessage();
        res.setUid(user.getId());
        session.sendMessage(res);

        EventUtil.executeEvent(EventType.LOGIN, user);
    }


    private User createUser(String loginName) {
        User user = new User();
        long id = IDUtil.getId();
        user.setId(id);
        user.setLoginName(loginName);
        DbData.insertData(user, true);
        DbData.registerUser(user);
        return user;
    }

    public void logout(User user) {
        DbData.updateData(user.getId(), DbDataType.USER, false);

        EventUtil.executeEvent(EventType.LOGOUT, user);
    }
}
