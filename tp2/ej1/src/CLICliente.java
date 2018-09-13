public class CLICliente {
    public static void main(String[] args) {
        if(args.length != 4) {
            handleError();
        }

        String  host = args[0],
                op = args[2];
        int n1 = 0, 
            n2 = 0;
        try {
            n1 = Integer.parseInt(args[1]);
            n2 = Integer.parseInt(args[3]);
        } catch (NumberFormatException e) {
            System.out.println("Necesita ingresar dos números y un operador");
            System.exit(1);
        }

        Cliente cliente = new Cliente(host);
        int res = 0;

        try {
            switch (op) {
                case "+":
                    res = cliente.sumar(n1, n2);
                    break;
                case "-":
                    res = cliente.restar(n1, n2);
                    break;
                case "*":
                    res = cliente.multiplicar(n1, n2);
                    break;
                case "/":
                    res = cliente.dividir(n1, n2);
                    break;
                default:
                    System.out.println("Operador incorrecto");
                    System.exit(1);
            }

            System.out.println(String.format(
                        "El resultado de la operación [%d %s %d] es igual a %d",
                        n1,
                        op,
                        n2,
                        res
                        ));
        } catch (NoSePudoCompletarLaOperacionException e) {
            System.out.println("No se pudo completar la operación :(");
            System.exit(1);
        }

    }
    public static void printHelp() {
        System.out.println(
                "USAGE: java CLICliente <SERVER_HOST> <N1> <OP> <N2>"
                );
    }
    public static void handleError() {
        printHelp();
        System.exit(1);
    }
}
