package com.lar.system.menu;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<MenuEntity, String> {
    @Query(
            value = """
                                           SELECT DISTINCT m.`perms` FROM sys_user_role ur
                        
                    //                        LEFT JOIN `sys_role` r ON ur.`role_id` = r.`id`
                    //                        LEFT JOIN `sys_role_menu` rm ON ur.`role_id` = rm.`role_id`
                    //                        LEFT JOIN `sys_menu` m ON m.`id` = rm.`menu_id`
                    //                    WHERE
                    //                        user_id = #{id}
                    //                        AND r.`status` = 0
                    //                       AND m.`status` = 0
                                           """,
            nativeQuery = true)
    List<String> selectPermissionById(String id);
}
