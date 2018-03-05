import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;


public class ex2 {
    private static final String extension=".zip";
    public static void main(String args[]) throws Exception {
        String path = null;
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            path = bufferedReader.readLine();
            File dir=new File(path);
            ex2.FindZipFile(dir);
        } catch (Exception e) {
            System.out.print("Error");
        }
    }

    public static void FindZipFile(File folder){
        File[] folderEntries=folder.listFiles();
        String[] zips=folder.list(new ExtensionFilter(extension));
        if (zips.length!=0){
            for(int i=0;i<zips.length;i++) {
                System.out.println(zips[i]);
                System.out.println(folder.getAbsolutePath()+"/"+zips[i]);        //Absolute path to the file
                unpackZip(folder.getAbsolutePath(),zips[i]);
            }
        }
        for (File entry: folderEntries){
            if (entry.isDirectory()){
                FindZipFile(entry);
                continue;
            }
        }
    }
    private static class ExtensionFilter implements FilenameFilter{
        private final String extension;
        public ExtensionFilter(String ext){
            extension=ext;
        }
        @Override
        public boolean accept(File dir, String name){
            return name.endsWith(extension);
        }
    }

    private static boolean unpackZip(String path, String zipname) {
        InputStream is;
        ZipInputStream zis;
        try {
            is = new FileInputStream(path + "/"+ zipname);
            zis = new ZipInputStream(new BufferedInputStream(is));
            ZipEntry ze;

            while((ze = zis.getNextEntry()) != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int count;

                String filename = ze.getName();
                if(ze.isDirectory()) {
                    File directPath = new File(path + zipname.substring(0,zipname.length()-4) + "\\" + filename);
                    directPath.mkdirs();
                } else {
                    File directPath = new File(path + zipname.substring(0,zipname.length()-4) + "\\");
                    directPath.mkdir();
                    FileOutputStream fout = new FileOutputStream(path + zipname.substring(0,zipname.length()-4) + "\\" + filename);

                    // reading and writing
                    while((count = zis.read(buffer)) != -1)
                    {
                        baos.write(buffer, 0, count);
                        byte[] bytes = baos.toByteArray();
                        fout.write(bytes);
                        baos.reset();
                    }

                    fout.close();
                    zis.closeEntry();
                }
            }

            zis.close();
        }
        catch(IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}


