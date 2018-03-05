/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apt_project;

import java.util.List;

/**
 *
 * @author Brenda
 */
public class Bundle {
    
    private String url;
    private List<String> childs;
    private String parent;
    private String robots;
    private String description ;
    private String title;
    private String header;
    private String body;
    
    Bundle()
    {
        url="";
        parent="";
        robots="";
        description="";
        title="";
        header="";
        body="";
    }
    public void setChild(List<String> ch)
    {
        for(int i=0 ; i<ch.size();i++)
        {
            childs.add(ch.get(i));
        }
        
    }
    public String getChild(int i)
    {
        return childs.get(i);
    }
    public int getChildCount()
    {
        return childs.size();
    }
    public void setUrl(String url)
    {
        this.url=url;
    }
    public void setParent(String parent)
    {
        this.parent=parent;
    }
    public void setRobots(String robots)
    {
        this.robots=robots;
    }
    public void setDescription(String desc)
    {
        this.description=desc;
    }
    public void setTitle(String title)
    {
        this.title=title;
    }
    public void setHeader(String header)
    {
        this.header=header;
    }
    public void setBody(String body)
    {
        this.body=body;
    }
            
    
}
