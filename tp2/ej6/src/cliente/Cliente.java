package cliente;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;

import reloj.IReloj;
import exceptions.ClienteException;

public class Cliente {

    private String host;
    private IReloj reloj;
    private long _time;
    private long updateTime;

    public Cliente(String host) throws ClienteException {
        try {
            this._time = System.currentTimeMillis();
            this.updateTime = 1000;
            this.setHost(host);
            this.reloj = (IReloj) Naming.lookup(this.getRname());
        } catch (RemoteException
                | NotBoundException
                | MalformedURLException e) {
            throw new ClienteException(e);
        }
    }

    public void adjustDrift(long adjustment) {
        if (this.updateTime + adjustment < 100) {
            this.updateTime = 100;
            return;
        }

        this.updateTime += adjustment;
    }

    public void tick() {
        this._time += 1000;
    }

    public long getTime() throws ClienteException {
        return this._time;
    }

    public long getPatternTime() throws ClienteException {
        try {
            return this.reloj.now();
        } catch (RemoteException e) {
            throw new ClienteException(e);
        }
    }

    public long getUpdateTime() {
        return this.updateTime;
    }

    public String getRname() {
        String rname = String.format(
            "//%s:%d/Reloj",
            this.getHost(),
            Registry.REGISTRY_PORT);
        return rname;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getHost() {
        return host;
    }
}
