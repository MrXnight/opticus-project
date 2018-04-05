import java.io.File;
import javax.swing.filechooser.FileFilter;
/*
Cette classe sert seulement pour filtrer les extensions de fichiers dans le JFileChooser
*/
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
               if ((path.endsWith(EXTENSION) && (path.charAt(path.length()- EXTENSION.length() - 1)) == '.')) { //On renvoie true seulemnt si l'extension est .opticus"
                    return true;
               }
          }
          return false; 
     }
     public String getDescription(){
          return description;
     }
}
