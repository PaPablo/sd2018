public class MainCliente {
    public static void main(String[] args) {
        Cliente cliente = new Cliente();

        for (int i = 0; i < 10; i++) {
            try {
                cliente.getHora();
                Thread.sleep(1000);
            } catch (InterruptedException e) {}
        }
    }
}
