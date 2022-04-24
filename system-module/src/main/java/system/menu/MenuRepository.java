package system.menu;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MenuRepository extends JpaRepository<MenuEntity, String> {
  @Query(
      value =
          "SELECT\n"
              + "            DISTINCT m.`perms`\n"
              + "        FROM\n"
              + "            sys_user_role ur\n"
              + "            LEFT JOIN `sys_role` r ON ur.`role_id` = r.`id`\n"
              + "            LEFT JOIN `sys_role_menu` rm ON ur.`role_id` = rm.`role_id`\n"
              + "            LEFT JOIN `sys_menu` m ON m.`id` = rm.`menu_id`\n"
              + "        WHERE\n"
              + "            user_id = #{id}\n"
              + "            AND r.`status` = 0\n"
              + "            AND m.`status` = 0",
      nativeQuery = true)
  List<String> selectPermissionById(String id);
}
