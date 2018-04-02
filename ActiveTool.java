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

//La méthode ActiveTool est une énumération, elle possède en attribut plusieurs chaines de caractères et un nom qui est susceptible de changer en prenant la valeur d'une de ses chaines.
//C'est cet objet qui stockera l'information de quel outil est actif
