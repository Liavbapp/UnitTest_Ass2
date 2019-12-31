
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SpaceTest {

    private int SIZE=20;
    private Space space;
    @Before
    public void init() {
         FileSystem fileSystem = new FileSystem(SIZE);
         space = FileSystem.fileStorage;
    }

    @Test
    public void testAlloc_Valid() throws Exception{
        Leaf leaf1 = new Leaf("file1",3);
        assertEquals(3,fileNumberBlocks("file1"));
        assertEquals(3,leaf1.allocations.length);

        Leaf leaf2 = new Leaf("file2",17);
        assertEquals(17,fileNumberBlocks("file2"));
        assertEquals(17,leaf2.allocations.length);

    }


    @Test
    public void testDealloc() throws Exception{
        Tree t = new Tree("root");
        t.depth = 0;
        Leaf leaf1 =new Leaf("file3",10);
        leaf1.parent = t;
        leaf1.depth = 1;
        t.children.put("file3",leaf1);

        Tree child_tree = new Tree("dir3");
        child_tree.parent = t;
        child_tree.depth = 1;
        t.children.put("dir3",child_tree);

        Leaf leaf2 = new Leaf("file5",8);
        leaf2.parent = child_tree;
        leaf2.depth = leaf2.parent.depth+1;
        child_tree.children.put("file5",leaf2);
        assertEquals(10,fileNumberBlocks("file3"));
        assertEquals(8,fileNumberBlocks("file5"));
        assertEquals(10,leaf1.allocations.length);
        assertEquals(8,leaf2.allocations.length);


        space.Dealloc(leaf1);
        Leaf[] leafAlloc = space.getAlloc();
        for (Integer blockIndex : leaf1.allocations)
            assertNull(leafAlloc[blockIndex]);
        assertFalse(t.children.containsKey(leaf1.name));
        assertEquals(0,fileNumberBlocks("file3"));

        space.Dealloc(leaf2);
        for (Integer blockIdx : leaf2.allocations)
            assertNull(leafAlloc[blockIdx]);
        assertFalse(child_tree.children.containsKey(leaf2.name));
        assertEquals(0,fileNumberBlocks("file5"));
    }

    @Test
    public void testCountFreeSpace() throws Exception{
        Leaf l = new Leaf("leaf",1);
        assertEquals(19,space.countFreeSpace());
        Leaf l2 = new Leaf("leaf2",19);
        assertEquals(0,space.countFreeSpace());
    }



    private int fileNumberBlocks(String name){
        int c = 0;
        for(Leaf leaf: space.getAlloc()){
            if(leaf!=null){
                if(leaf.name.equals(name))
                    c++;
            }
        }
        return c;
    }
}
