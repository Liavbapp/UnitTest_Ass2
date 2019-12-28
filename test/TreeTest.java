
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TreeTest {

    private Tree regTree ;
    @Before
    public void init() throws OutOfSpaceException {
        FileSystem fileSystem = new FileSystem(30);

        regTree = new Tree("root");
        Tree child_1 = new Tree("dir1");
        Leaf leaf_1 = new Leaf("file1",4);
        leaf_1.parent = child_1;
        child_1.children.put("file1",leaf_1);
        Leaf leaf_1_1 = new Leaf("file1_1",4);
        leaf_1_1.parent = child_1;
        child_1.children.put("file1_1",leaf_1_1);
        child_1.depth= 1;
        leaf_1.depth = 2;
        leaf_1_1.depth = 2;
        child_1.parent =regTree;

        Tree child_2 = new Tree("dir2");
        Leaf leaf_2 = new Leaf("file2",3);
        leaf_2.parent = child_2;
        child_2.children.put("file2",leaf_2);
        child_2.depth = 1;
        leaf_2.depth = 2;
        child_2.parent = regTree;

        Tree child_3 = new Tree("dir3");
        Leaf leaf3 = new Leaf("file3",4);
        leaf3.parent = child_3;
        child_3.children.put("file3",leaf3);
        child_3.depth = 1;
        leaf3.depth = 2;
        child_3.parent = regTree;

        regTree.children.put("dir1",child_1);
        regTree.children.put("dir2",child_2);
        regTree.children.put("dir3",child_3);
        regTree.depth=0;
        regTree.parent = null;
    }

    @Test
    public void testGetChildByName(){
        //existing child
        Tree child = regTree.GetChildByName("dir2");
        assertEquals(child.depth , 1);
        assertEquals(child.parent.name,"root");
        assertTrue(child.children.containsKey("file2"));

        //non existing child
        Tree newChild = regTree.GetChildByName("dir4");
        assertEquals(newChild.depth,1);
        assertEquals(newChild.parent.name,"root");
        assertTrue(regTree.children.containsKey("dir4"));

        //non existing newChild_child
        Tree newChild_child =  newChild.GetChildByName("dir6");
        assertEquals(newChild_child.depth,2);
        assertEquals(newChild_child.parent.name,"dir4");
        assertTrue(newChild.children.containsKey("dir6"));
    }

    //should contain root dir?>?!!??
    @Test
    public void testGetPathTreeNode(){
        Tree child1 = (Tree) regTree.children.get("dir1");
        Tree child2 = (Tree) regTree.children.get("dir2");
        assertArrayEquals(new String[]{"dir1"},child1.getPath());
        assertArrayEquals(new String[]{"dir1","file1"},child1.children.get("file1").getPath());
        assertArrayEquals(new String[]{"dir1","file1_1"},child1.children.get("file1_1").getPath());
        assertArrayEquals(new String[]{"dir2","file2"},child2.children.get("file2").getPath());
    }





}
