public enum ActiveTool{
     LENTILLE("Lentille"),SOURCE("Source"),NULL("Aucun"),SELECT("Select");

     private String name;

     ActiveTool(String name){
          this.name = name;
     }

     public String toString(){
          return("Outil "+name);
     }

}
