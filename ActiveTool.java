public enum ActiveTool{
     LENTILLE("Lentille"),SOURCE("Source"),MIROIR("Miroir"),NULL("Aucun"),SELECT("Select"),SUPPR("Supprimer"), SCREENSHOT("CaptureEcran");

     private String name;

     ActiveTool(String name){
          this.name = name;
     }

     public String toString(){
          return("Outil "+name);
     }

}
