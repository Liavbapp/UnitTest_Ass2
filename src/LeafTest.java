import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.nio.file.DirectoryNotEmptyException;
import java.util.Arrays;
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

    //we are expecting here OutOfSpace exception, but null pointer exception is returned
    @Test(expected = OutOfSpaceException.class)
    public void testInitInvalidLeaf() throws Exception{
        //init leaf with overflow space
        Leaf l = new Leaf("file2",12);
    }


}
