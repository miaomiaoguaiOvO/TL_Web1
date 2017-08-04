package com.ke.controller.bs;

import com.ke.pojo.User;
import com.ke.service.UserService;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/8/3.
 */

@Controller
@RequiresRoles("admin")
@RequestMapping("/bs")
public class BsUserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/manageuserpage",method = RequestMethod.GET)
    public String manageUser(Model model){

        List<User> users = userService.selectAllUser();

        model.addAttribute("users",users);

        return "bs/admin/manage-user";
    }

    @RequestMapping(value = "/manageadminpage",method = RequestMethod.GET)
    public String manageAdmin(Model model){

        List<User> admins = userService.selectAllAdmin();

        model.addAttribute("admins",admins);
        model.addAttribute("count",admins.size());

        return "bs/admin/manage-admin";
    }

    @RequestMapping(value = "/managemoderatorpage",method = RequestMethod.GET)
    public String manageModerator(Model model){

        List<User> moderators = userService.selectAllModerator();

        model.addAttribute("moderators",moderators);

        return "bs/admin/manage-moderator";
    }


    @RequestMapping(value = "/adduser",method = RequestMethod.POST)
    public String addUser(User user,Model model){

        System.out.println("添加用户"+user.getRoleId()+user.getUsername()+user.getPassword());

        user.setRegistrationTime(new Date());
        user.setLocked(0);
        user.setLoginTimes(0);
        user.setSalt("123");

        userService.insertUser(user);

        int i = user.getRoleId();
        switch (i){
            case 1 :
                model.addAttribute("users",userService.selectAllAdmin());
                return "bs/admin/manage-admin";

            case 2 :
                model.addAttribute("users",userService.selectAllModerator());
                return "redirect:/bs/managemoderatorpage";

            case 3 :
                model.addAttribute("users",userService.selectAllUser());
                return "redirect:/bs/manageuserpage";

            default:return "bs/admin/index";
        }

    }
}
