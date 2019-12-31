
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LeafTest {

   private FileSystem fileSystem ;
    @Before
    public void init() {
        fileSystem = new FileSystem(10);
    }

    @Test
    public void testInitValidLeaf() throws Exception{
        Leaf l =new Leaf("file100",10);
        assertEquals(FileSystem.fileStorage.countFreeSpace(),0);
        assertEquals(l.name,"file100");
    }

    @Test
    public void testInitInvalidLeaf() throws Exception{

        try{
            //should throw outOfSpace exception, if no exception then test should fail
            Leaf leaf = new Leaf("file2",12);
            fail();
        }catch (Exception ex){
            //checking the correct exception thrown
            assertEquals(OutOfSpaceException.class,ex.getClass());
        }

    }



}
