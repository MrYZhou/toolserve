<#--我是一个freemarker注释-->
<#--
<#list datas as item>
    此处的 _index是默认的语法糖，代表下标
    ${item_index+1}
</#list>
-->
public class ${package?capitalize}Controller{

@Autowired ${package?capitalize}Service ${package}Service;
<#assign data = "${json}" />
<#if data??>
    ${data}
<#else>
    none
</#if>

}