public class RemoteFile {
    private String name;
    private Boolean isOpen;

    private int currentIndex;

    public RemoteFile(int name) {
        this.name = name;
        this.isOpen = true;
        this.currentIndex = 0;
    }

    public int open() {
        return 0;
    }

    public int close() {
        return 1;
    }

    public byte[] read() {
        byte[] buffer = new byte[3];
        return buffer;
    }

}
