package servidor;

public class MainServidor {
    public static void main(String[] args) {

        String host = "localhost";
        int offset = 0;

        for (int i = 0; i < args.length; i++) {
            try {
                if (args[i].equals("-h") || args[i].equals("--host")) {
                    i++;
                    host = args[i];
                    continue;
                } 
                if (args[i].equals("-o") || args[i].equals("--offset")) {
                    i++;
                    offset = Integer.parseInt(args[i]);
                    continue;
                } 
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Invocación incorrecta - faltan parámetros");
                printHelp();
                System.exit(1);
            } catch (NumberFormatException e) {
                System.out.println("Invocación incorrecta - el parámetro OFFSET debe ser un número");
                printHelp();
                System.exit(1);
            }
            
        }
        System.out.println(String.format(
                    "Servidor puesto a la escucha en [%s]",
                    host));
        new Servidor(host, offset);
    }

    public static void printHelp() {
        System.out.println("MainServidor [-h|--host] [HOST] [-o|--offset] [N]");
    }
}
