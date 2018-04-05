public enum ActiveTool{
     LENTILLE("Lentille"),SOURCE("Source"),MIROIR("Miroir"),NULL("Aucun"),SELECT("Select"),SUPPR("Supprimer"), SCREENSHOT("CaptureEcran"); // les différents outils actifs

     private String name;

     ActiveTool(String name){
          this.name = name;
     }

     public String toString(){
          return("Outil "+name);
     }

}

//La méthode ActiveTool est une énumération, elle va définir les valeurs que va pouvoir prendre l'attribut activeTool qui définit l'outil actif
