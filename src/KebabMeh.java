import org.powerbot.script.*;
import org.powerbot.script.rt4.*;
import org.powerbot.script.rt4.ClientContext;

import java.awt.*;
import java.util.concurrent.Callable;


@Script.Manifest(name = "KebabMeh", description = "Buys Kebabs from Karim", properties = "client=4")

public class KebabMeh extends PollingScript<ClientContext> implements PaintListener, MessageListener {
    private final int kebabId = 1971;
    private final int karimId = 529;
    private final int kebabPrice = 304;
    private final int closedDoor = 7122;
    private final int bankerIds[] = {396, 397};
    private int kebabPurchased, profit;

    final Tile[] pathToShop = {new Tile(3269, 3167), new Tile(3276, 3170), new Tile(3274, 3180)};

    private String currentTime = "00:00:00";
    private String status;

    private final long initTime = System.currentTimeMillis();

    private double runTime, kebabPerHour, profitPerHour;

    @Override
    public void messaged(MessageEvent m) {
        if (m.text().startsWith("You buy a")) {
            kebabPurchased++;
            profit += kebabPrice - 1;
        }
    }

    @Override
    public void poll() {
        switch (state()) {
            case BUY:
                Npc Karim = ctx.npcs.select().id(karimId).nearest().poll();
                GameObject door = ctx.objects.at(new Tile(3276, 3180)).select().id(closedDoor).nearest().poll();


                if (Karim.inViewport() && !ctx.players.local().inMotion()) {
                    if (door.inViewport()) {
                        status = "Opening Door";
                        door.interact("Open");
                        Condition.sleep(500);
                    } else {
                        status = "Buying Kebabs";
                        Karim.interact("talk");
                        Condition.sleep(600);
                    }
                    if (ctx.widgets.component(231, 2).valid()) {
                        ctx.widgets.component(231, 2).click(true);
                    }
                    Condition.sleep(600);
                    if (ctx.widgets.component(219, 0).component(2).valid()) {
                        ctx.widgets.component(219, 0).component(2).click(true);
                    }
                    Condition.sleep(600);
                    if (ctx.widgets.component(217, 2).valid()) {
                        ctx.widgets.component(217, 2).click(true);
                    }
                    Condition.sleep(600);
                } else {
                    status = "Walking to Karim";
                    ctx.movement.newTilePath(pathToShop).traverse();
                    ctx.camera.turnTo(Karim);
                }
                break;

            case BANK:
                Npc Banker = ctx.npcs.select().id(bankerIds).nearest().poll();
                if (Banker.inViewport() && !ctx.players.local().inMotion()) {
                    if (ctx.bank.opened()) {
                        if (ctx.inventory.id(kebabId).count() > 1) {
                            status = "Depositing Kebabs";
                            ctx.bank.deposit(kebabId, Bank.Amount.ALL);
                        }
                        ctx.bank.close();
                    } else {
                        status = "Opening Bank";
                        Banker.interact("Bank");
                        Condition.wait(new Callable<Boolean>() {
                            @Override
                            public Boolean call() throws Exception {
                                return ctx.bank.opened();
                            }
                        });
                    }
                } else {
                    status = "Walking to Bank";
                    ctx.movement.newTilePath(pathToShop).reverse().traverse();
                    ctx.camera.turnTo(Banker);
                }
                break;
        }
    }

    private State state() {
        if (ctx.inventory.select().count() < 28) {
            return State.BUY;
        }
        return State.BANK;
    }

    private enum State {
        BUY, BANK
    }

    private final Font font1 = new Font("Arial Narrow", 0, 12);

    private final Color color1 = new Color(255, 255, 255);
    private final Color color2 = new Color(0, 0, 0, 130);

    @Override
    public void repaint(Graphics g1) {
        int hours = (int) (System.currentTimeMillis() - initTime) / 3600000;
        int minutes = (int) (System.currentTimeMillis() - initTime) / 60000 - hours * 60;
        int seconds = (int) (System.currentTimeMillis() - initTime) / 1000 - hours * 3600 - minutes * 60;
        currentTime = String.format("%02d", hours) + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds);

        runTime = (double) (System.currentTimeMillis() - initTime) / 3600000;
        kebabPerHour = kebabPurchased / runTime;
        profitPerHour = kebabPrice * kebabPerHour;

        Graphics2D g = (Graphics2D) g1;
        g.setColor(color2);
        g.fillRect(10, 10, 120, 110);
        g.setColor(color1);
        g.setFont(font1);

        g.drawString("KebabMeh", 45, 25);
        g.drawString("Purchased: " + kebabPurchased + "(" + (int) kebabPerHour + "/hour)", 15, 45);
        g.drawString("Profit: " + profit + "(" + (int) profitPerHour + "/hour)", 15, 65);
        g.drawString("Status: " + status, 15, 85);
        g.drawString("RunTime: " + currentTime, 15, 105);

        g.setColor(Color.GREEN);
        final Point p = ctx.input.getLocation();
        g.drawLine(p.x - 5, p.y - 5, p.x + 5, p.y + 5);
        g.drawLine(p.x - 5, p.y + 5, p.x + 5, p.y - 5);

    }
}
