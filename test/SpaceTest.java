
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SpaceTest {

    int SIZE=20;
    Space space;
    @Before
    public void init() {
         FileSystem fileSystem = new FileSystem(SIZE);
         space = FileSystem.fileStorage;
    }

    @Test
    public void testAlloc_Valid() throws Exception{
        Leaf leaf1 = new Leaf("file1",3);
        assertEquals(fileNumberBlocks("file1"),3);
        assertEquals(leaf1.allocations.length,3);

        Leaf leaf2 = new Leaf("file2",17);
        assertEquals(fileNumberBlocks("file2"),17);
        assertEquals(leaf2.allocations.length,17);

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
        assertEquals(fileNumberBlocks("file3"),10);
        assertEquals(fileNumberBlocks("file5"),8);
        assertEquals(leaf1.allocations.length,10);
        assertEquals(leaf2.allocations.length,8);


        space.Dealloc(leaf1);
        Leaf[] leafAlloc = space.getAlloc();
        for (Integer blockIndex : leaf1.allocations)
            assertNull(leafAlloc[blockIndex]);
        assertFalse(t.children.containsKey(leaf1.name));
        assertEquals(fileNumberBlocks("file3"),0);

        space.Dealloc(leaf2);
        for (Integer blockIdx : leaf2.allocations)
            assertNull(leafAlloc[blockIdx]);
        assertFalse(child_tree.children.containsKey(leaf2.name));
        assertEquals(fileNumberBlocks("file5"),0);
    }

    @Test
    public void testCountFreeSpace() throws Exception{
        Leaf l = new Leaf("leaf",1);
        assertEquals(space.countFreeSpace(),19);
        Leaf l2 = new Leaf("leaf2",19);
        assertEquals(space.countFreeSpace(),0);
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
