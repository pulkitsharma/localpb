package scripts;
import org.powerbot.script.Condition;
import org.powerbot.script.Filter;
import org.powerbot.script.MessageEvent;
import org.powerbot.script.MessageListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.*;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.List;
import java.util.Arrays;

@Script.Manifest(name = "Fletch Headless Arrows", description = "Fletch Headless Arrows")
public class FletchWillowToArrowShaft extends PollingScript<ClientContext> implements MessageListener {
    private int step = 1;
    private int willowLogId = 1511;
    private int arrowShaftId = 52;
    private int featherId = 314;
    private static final Random rand = new Random();

    @Override
    public void poll() {
        final State state = getState();
        if (state == null) {
            return;
        }
        switch (state) {
            case TELE_SARIM:
                ctx.widgets.component(1477,72).component(1).click();
                sleep(1500);
                ctx.widgets.component(1092,18).click();
                sleep(21000);
                step=1;
                break;
            case WITHDRAW_WILLOW: 
                if(!ctx.bank.opened()){
					ctx.bank.open();
					sleep(1000);
				}
				ctx.bank.withdraw(willowLogId, 28);
				sleep(1000);
				ctx.bank.close();
				sleep(1000);
				step=2;
                break;
            case MAKE_ARROWS:
                Item log = ctx.backpack.select().id(willowLogId).poll();
                if (log.interact("Craft")) {
                    sleep(2000);
                    if(ctx.widgets.component(1179,36).component(1).visible()){
                        ctx.widgets.component(1179,36).component(1).click();
                    }
                    sleep(1500);
                    if (!ctx.players.local().inMotion() && ctx.players.local().animation() == -1) {
                        ctx.widgets.component(1371,44).component(0).click();
                        sleep(2000);
                        ctx.widgets.component(1370,20).click();
                        sleep(50000);
                    }
                }
                step=3;
                break;
            case BANK_ARROW:
                if(!ctx.bank.opened()){
					ctx.bank.open();
					sleep(3000);
				}
				if(rand.nextInt(100) < 95){
					ctx.bank.depositInventory();
					Condition.sleep(rand.nextInt(1000) + 300);
				}
				else{
					ctx.bank.deposit(arrowShaftId, 28);
					Condition.sleep(rand.nextInt(1000) + 300);
				}
				step=1;
                break;
            default:
                break;
           
        }
    }

    private enum State {
        TELE_SARIM, WITHDRAW_WILLOW, MAKE_ARROWS, BANK_ARROW  
    }

    private State getState() {
        switch(step){
        case 0:
            return State.TELE_SARIM;
        case 1:
            return State.WITHDRAW_WILLOW;
        case 2:
            return State.MAKE_ARROWS;
        case 3:
            return State.BANK_ARROW;
        default:
            return null;
        }
    }

    @Override
    public void messaged(MessageEvent e) {
        final String msg = e.text().toLowerCase();
    }

    private void sleep(int sleep){
        try {
            Thread.sleep(sleep);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
}

