public class MainServidor {
    public static void main(String[] args) {
        String host = "localhost";
        for (int i = 0; i < args.length; i++) {
            try {
                if (args[i].equals("-h") || args[i].equals("--host")) {
                    i++;
                    host = args[i];
                } 
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Invocación incorrecta - faltan parámetros");
                System.exit(1);
            }
        }
        System.out.println(String.format(
                    "Servidor puesto a la escucha en [%s]",
                    host));
        new Servidor(host);
    }
}
