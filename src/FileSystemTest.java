import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.nio.file.DirectoryNotEmptyException;
import java.util.Arrays;

import static org.junit.Assert.*;

public class FileSystemTest {
    FileSystem fileSystem;
    private String [] dir1_valid = new String[]{"root","dir1"};
    private String [] dir2_valid = new String[]{"root","dir1","dir2"};
    private String [] validfile_dir2 = new String[]{"root","dir1","dir2","file1"};
    private String [] dir3_valid = new String[]{"root","dir1","dir2","dir3"};
    private String [] validfile_dir3 =new String[]{"root","dir1","dir2","dir3","file1"};
    private String [] validfile2_dir3 =new String[]{"root","dir1","dir2","dir3","file2"};
    private String [] bad_dir_name_nRoot = new String[]{"dir1","dir2"};
    private String [] bad_file_name_nRoot = new String[]{"dir1","dir2","dir3","file1"};


    @Before
    public void init(){
        fileSystem = new FileSystem(10);
    }
    @After
    public void clean(){

    }

    @Test
    public void test_FileExist() throws Exception{
        fileSystem.dir(dir3_valid);
        //not existing file
        assertNull(fileSystem.FileExists(validfile_dir3));
        //existing file
        fileSystem.file(validfile_dir3,5);
        assertNotNull(fileSystem.FileExists(validfile_dir3));
    }

    @Test
    public void test_DirExist() throws Exception{

        //not existing dir
        assertNull(fileSystem.DirExists(dir3_valid));
        //existing dir
        fileSystem.dir(dir3_valid);
        assertNotNull(fileSystem.DirExists(dir3_valid));
    }

   @Test
    public void testValid_dir() throws Exception{
        fileSystem.dir(dir1_valid);
        assertArrayEquals(new String [] {"root","dir1"},fileSystem.DirExists(dir1_valid).getPath());
        fileSystem.dir(dir3_valid);
        assertArrayEquals(new String [] {"root","dir1","dir2","dir3"},fileSystem.DirExists(dir3_valid).getPath());

        //add an already exist dir
       fileSystem.dir(dir3_valid);
       assertArrayEquals(new String [] {"root","dir1","dir2","dir3"},fileSystem.DirExists(dir3_valid).getPath());
   }
    @Test (expected = BadFileNameException.class)
    public void test_dirUnRooted() throws Exception {
        fileSystem.dir(bad_dir_name_nRoot);
    }

    @Test (expected = BadFileNameException.class)
    public void test_dirFileExists() throws Exception{
        fileSystem.dir(validfile_dir2);
        fileSystem.file(dir3_valid,5);
        fileSystem.dir(dir3_valid);
    }

    @Test
    public void test_lstdir() throws Exception{
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
    public void test_rmdir() throws Exception {
        //remove not existing dir, should so anything
        fileSystem.rmfile(dir2_valid);
        //remove existing empty dir
        fileSystem.dir(dir2_valid);
        fileSystem.rmdir(dir2_valid);
        assertNull(fileSystem.DirExists(dir2_valid));
    }

    @Test(expected = DirectoryNotEmptyException.class)
    public void test_rmdir_notEmpty() throws Exception{
        fileSystem.dir(dir2_valid);
        fileSystem.file(validfile_dir2,3);
        fileSystem.rmdir(dir2_valid);
    }

    @Test
    public void test_rmfile() throws Exception{
        fileSystem.dir(dir3_valid);
        //removing nonexisting file
        fileSystem.rmfile(validfile_dir3);

        fileSystem.file(validfile_dir3,3);
        fileSystem.rmfile(validfile_dir3);
        assertNull(fileSystem.FileExists(validfile_dir3));
    }

    


//   @Test
//   public void testInit(){
//        assertEquals(FileSystem.fileStorage.countFreeSpace(),10);
//   }

   @Test(expected = OutOfSpaceException.class)
   public void testCreateOutOfSpaceFile() throws Exception{
        fileSystem.dir(dir3_valid);
        fileSystem.file(validfile_dir3,11);
   }


    //should throw BadFileName exception but throw another exception
    @Test(expected = OutOfSpaceException.class)
    public void testCreateOutOfSpaceFile3() throws Exception{
        fileSystem.dir(dir3_valid);
        fileSystem.file(validfile_dir3,4);
        fileSystem.file(validfile2_dir3,5);
        fileSystem.file(validfile_dir3,7);
        //the file shouldn't be overwrite so the free space should remain 1
        assertEquals(FileSystem.fileStorage.countFreeSpace(),1);
    }

    @Test(expected = BadFileNameException.class)
    public void testFileBadName() throws Exception{
        fileSystem.dir(dir3_valid);
        fileSystem.file(bad_file_name_nRoot,3);
        fileSystem.dir(dir3_valid);
    }

    //should throw BadFileName exception but throw ClassCastException
    @Test(expected = BadFileNameException.class)
        public void testFileWithExistingFolderName() throws Exception{
        fileSystem.dir(dir3_valid);
        fileSystem.file(dir2_valid,3);
    }


    @Test
    public void testValidFile() throws Exception{
        fileSystem.dir(dir3_valid);
        fileSystem.file(validfile_dir3,5);
        assertEquals(fileSystem.lsdir(dir3_valid)[0],"file1");
        assertEquals(FileSystem.fileStorage.countFreeSpace(),5);
        assertTrue(inDisk(validfile_dir3));
        //overwrite file
        fileSystem.file(validfile_dir3,7);
        assertEquals(fileSystem.lsdir(dir3_valid)[0],"file1");
        assertEquals(FileSystem.fileStorage.countFreeSpace(),3);
        assertTrue(inDisk(validfile_dir3));
    }


   @Test
   public void testFile() throws Exception{
        fileSystem.dir(dir1_valid);
   }


//    @Test
//    public void testRemoveFile() {
//        fileSystem.rmfile();
//    }

    @Test
    public void testremoveEmptyDir() throws Exception {
        fileSystem.dir(dir3_valid);
        fileSystem.rmdir(dir3_valid);
        assertEquals(fileSystem.lsdir(dir2_valid).length,0);
    }
    @Test(expected = DirectoryNotEmptyException.class)
    public void testRemoveNotEmptyDir() throws  Exception{
        fileSystem.dir(dir3_valid);
        fileSystem.file(validfile_dir3,1);
        fileSystem.rmdir(dir3_valid);
    }
    @Test
    public void testRemoveNonExistingDir() throws Exception {
        fileSystem.rmdir(dir3_valid);
    }
    @Test
    public void testRemoveExistingFile() throws Exception {
        fileSystem.dir(dir3_valid);
        fileSystem.file(validfile_dir3,3);
        fileSystem.rmfile(validfile_dir3);
        assertEquals(FileSystem.fileStorage.countFreeSpace(),10);
    }
    @Test
    public void testRemoveNonExistingFile() throws Exception {
        fileSystem.dir(dir2_valid);
        fileSystem.rmfile(validfile_dir3);
        assertEquals(fileSystem.lsdir(dir1_valid).length,1);
    }

    @Test
    public void testLstNonExistingDir() throws Exception{
        fileSystem.dir(dir2_valid);
        assertNull(fileSystem.lsdir(dir3_valid));
    }

    @Test
    public void testLstExistingDir() throws Exception {
        fileSystem.dir(dir3_valid);
        assertArrayEquals(fileSystem.lsdir(dir2_valid),new String[]{"dir3"});
        fileSystem.file(validfile_dir3,2);
        assertArrayEquals(fileSystem.lsdir(dir3_valid), new String[]{"file1"});
        fileSystem.file(validfile2_dir3,2);
        assertArrayEquals(fileSystem.lsdir(dir3_valid),new String[]{"file1","file2"});
        assertArrayEquals(fileSystem.lsdir(dir2_valid),new String[]{"dir3"});
        fileSystem.file(validfile_dir2,1);
        assertArrayEquals(fileSystem.lsdir(dir2_valid),new String[]{"dir3","file1"});
    }

    @Test
    public void testDisk() throws Exception{
        String [] arrFree =new String[10];
        for(int i=0;i<arrFree.length;i++){
            arrFree[i]=null;
        }
        assertArrayEquals(fileSystem.disk(),arrFree);
        fileSystem.dir(dir1_valid);
        assertArrayEquals(fileSystem.disk(),arrFree);
        fileSystem.dir(dir2_valid);
        assertArrayEquals(fileSystem.disk(),arrFree);
        fileSystem.dir(dir3_valid);
        assertArrayEquals(fileSystem.disk(),arrFree);
        fileSystem.file(validfile_dir3,2);
        boolean fNameExist=inDisk(validfile_dir3);
        assertTrue(fNameExist);
    }

    private boolean inDisk(String []name){
        for (String [] file_name:fileSystem.disk()) {
            if(Arrays.equals(file_name,name)){
                return true;
            }
        }
        return false;
    }


}
