import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.nio.file.DirectoryNotEmptyException;
import java.util.Arrays;

import static org.junit.Assert.*;

public class FileSystemTest {
    private FileSystem fileSystem;
    private String [] dir1_valid = new String[]{"root","dir1"};
    private String [] dir2_valid = new String[]{"root","dir1","dir2"};
    private String [] validfile_dir2 = new String[]{"root","dir1","dir2","file1"};
    private String [] dir3_valid = new String[]{"root","dir1","dir2","dir3"};
    private String [] validfile_dir3 =new String[]{"root","dir1","dir2","dir3","file1"};
    private String [] validfile2_dir3 =new String[]{"root","dir1","dir2","dir3","file2"};
    private String [] bad_dir_name_nRoot = new String[]{"dir1","dir2"};
    private String [] bad_file_name_nRoot = new String[]{"dir1","dir2","dir3","file1"};


    @Before
    public void setup(){
        fileSystem = new FileSystem(10);
    }




    @Test
    public void testFileExist() throws Exception{
        fileSystem.dir(dir3_valid);
        //not existing file
        assertNull(fileSystem.FileExists(validfile_dir3));
        //existing file
        fileSystem.file(validfile_dir3,5);
        assertNotNull(fileSystem.FileExists(validfile_dir3));
    }

    @Test
    public void testDirExist() throws Exception{

        //not existing dir
        assertNull(fileSystem.DirExists(dir3_valid));
        //existing dir
        fileSystem.dir(dir3_valid);
        assertNotNull(fileSystem.DirExists(dir3_valid));
    }

   @Test
    public void testValidDir() throws Exception{
        fileSystem.dir(dir1_valid);
        assertArrayEquals(new String [] {"root","dir1"},fileSystem.DirExists(dir1_valid).getPath());
        fileSystem.dir(dir3_valid);
        assertArrayEquals(new String [] {"root","dir1","dir2","dir3"},fileSystem.DirExists(dir3_valid).getPath());

        //add an already exist dir
        //fileSystem.dir(dir3_valid);
//       assertArrayEquals(new String [] {"root","dir1","dir2","dir3"},fileSystem.DirExists(dir3_valid).getPath());
   }
    @Test (expected = BadFileNameException.class)
    public void testDirUnRooted() throws Exception {
        fileSystem.dir(bad_dir_name_nRoot);
    }

    @Test (expected = BadFileNameException.class)
    public void testDirFileExists() throws Exception{
        fileSystem.dir(validfile_dir2);
        fileSystem.file(dir3_valid,5);
        fileSystem.dir(dir3_valid);
    }

    @Test
    public void testLstdir() throws Exception{
        //lst non existing dir
        assertNull(fileSystem.lsdir(dir3_valid));
        //lst non empty dir
        fileSystem.dir(dir3_valid);
        assertEquals(0,fileSystem.lsdir(dir3_valid).length);
        //lst dir with files
        fileSystem.file(validfile_dir3,3);
        fileSystem.file(validfile2_dir3,2);
        String[] fileList = fileSystem.lsdir(dir3_valid);
        assertArrayEquals(new String[]{"file1","file2"},fileList);
    }

    @Test
    public void testRmdir() throws Exception {
        //remove not existing dir, should so anything
        fileSystem.rmfile(dir2_valid);
        //remove existing empty dir
        fileSystem.dir(dir2_valid);
        fileSystem.rmdir(dir2_valid);
        assertNull(fileSystem.DirExists(dir2_valid));
    }

    @Test(expected = DirectoryNotEmptyException.class)
    public void testRmdirNotEmpty() throws Exception{
        fileSystem.dir(dir2_valid);
        fileSystem.file(validfile_dir2,3);
        fileSystem.rmdir(dir2_valid);
    }

    @Test
    public void testRmfile() throws Exception{
        fileSystem.dir(dir3_valid);
        //removing nonexisting file
        fileSystem.rmfile(validfile_dir3);

        fileSystem.file(validfile_dir3,3);
        fileSystem.rmfile(validfile_dir3);
        assertNull(fileSystem.FileExists(validfile_dir3));
    }

    @Test
    public void testDisk() throws Exception{
        //checking null disk
        fileSystem.dir(dir3_valid);
        String[][] disk = fileSystem.disk();
        for(String[] str : disk)
            assertNull(str);
        //checking allocated disk
        fileSystem.file(validfile_dir3,4);
        disk = fileSystem.disk();
        int c = 0;
        for(String[] name : disk){
            if(name!=null)
                if(Arrays.equals(name,validfile_dir3))
                    c++;
        }
        assertEquals(4,c);
    }

    @Test
    public void testFile() throws Exception{
        //file with existing dirs
        fileSystem.dir(dir2_valid);
        fileSystem.file(validfile_dir2,3);
        assertNotNull(fileSystem.FileExists(validfile_dir2));
        //file with (some) non-existing dirs
        fileSystem.file(validfile2_dir3,3);
        assertNotNull(fileSystem.FileExists(validfile2_dir3));
        //overwriting a file
        fileSystem.file(validfile2_dir3,2);
        assertNotNull(fileSystem.FileExists(validfile2_dir3));
        assertEquals(5, FileSystem.fileStorage.countFreeSpace());
//        //try overwriting file with extra space, old file should remain. nullptr exception!
//        fileSystem.file(validfile2_dir3,8);
//        assertEquals(5,FileSystem.fileStorage.countFreeSpace());
    }

    @Test(expected = BadFileNameException.class)
    public void testFileBadName()throws Exception{
        //non root file name
        fileSystem.file(bad_file_name_nRoot,3);
    }

    @Test(expected = OutOfSpaceException.class)
    public void testFileOverAllocation1() throws Exception{
            fileSystem.file(validfile2_dir3,11);
    }

    @Test(expected = OutOfSpaceException.class)
    public void testFileOverAllocation2() throws Exception{
        fileSystem.file(validfile_dir3,5);
        fileSystem.file(validfile2_dir3,6);
    }

//    @Test(expected = BadFileNameException.class)
//    public void test_fileExistingDirName() {
//        try {
//            fileSystem.dir(dir3_valid);
//            fileSystem.file(dir2_valid,5);
//        }catch (Exception ex){
//
//        }
//
//    }

//    @Test
//    public void testInit(){
//        assertEquals(FileSystem.fileStorage.countFreeSpace(),10);
//    }



}
