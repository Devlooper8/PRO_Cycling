package sk.tondrus.multiThreading;

public abstract class Zamestnanec extends Thread {
    protected Semaphore semaphore;
    protected Udalost udalost;
    protected boolean koniec=false;
    protected int Cinnost;

    /**
     * konstruktor
     * @param meno meno zamestnanca
     * @param udalost udalost pri ktorej zacina pracovat
     * @param cinnost cinnost ktoru zacne vykonavat
     */
    public Zamestnanec(String meno, Udalost udalost,int cinnost){
        setName(meno);
        this.semaphore=new Semaphore();
        this.udalost=udalost;
        Cinnost=cinnost;
        udalost.zaregistrujZamestnanca(Cinnost,this);
    }

    /**
     * funkcia run zobudi thread a zacne pracovat
     */
    public void run() {
        System.out.println(getName() + " started");
        while(!koniec){
            semaphore.doWait();

            if (koniec) {
                break;
            }
            pracuj();
        }
        udalost.odregistrujZamestnanca(Cinnost,this);
        dokonci();

    }

    protected void pracuj(){

    }

    protected void dokonci(){
    }

    /**
     * zobudi thread
     */
    public void vstavaj(){
        semaphore.doNotify();
    }

    /**
     * zastavi thread
     */
    public final void zastav(){
        koniec=true;
        semaphore.doNotify();
    }
}
