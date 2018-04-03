import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

public class OpticusFilter extends FileFilter{
     public static final String EXTENSION = "opticus";
     private final String description = "OPTICUS";

     public OpticusFilter(){}

     public boolean accept(File file) {
          if (file.isDirectory()) {
               return true;
          }
          else {
               String path = file.getAbsolutePath().toLowerCase();
               if ((path.endsWith(EXTENSION) && (path.charAt(path.length()- EXTENSION.length() - 1)) == '.')) {
                    return true;
               }
          }
          return false;
     }
     public String getDescription(){
          return description;
     }
}
