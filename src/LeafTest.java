
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LeafTest {

    FileSystem fileSystem ;
    @Before
    public void init() {
        fileSystem = new FileSystem(10);
    }

    @Test
    public void testInitValidLeaf() throws Exception{
        Leaf l =new Leaf("file100",8);
        assertEquals(FileSystem.fileStorage.countFreeSpace(),2);
        assertEquals(l.name,"file100");
    }

    //should throw outofSpace exception (but never thrown in the code)
    @Test(expected = OutOfSpaceException.class)
    public void testInitInvalidLeaf() throws Exception{
        //init leaf with overflow space
        Leaf leaf = new Leaf("file2",12);
    }



}
