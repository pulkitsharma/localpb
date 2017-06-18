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
public class FletchBowStrings extends PollingScript<ClientContext> implements MessageListener {
    private int step = 1;
    private int logId = 1511;
    private int[] treeIds = {38787, 38783, 38788, 38782, 38760, 38785, 45798, 47594,47596,47598,47600};
    private int oakLogId = 1521;
    private int[] oakTreeId = {38732};
    private int[] deadOakTreeId = {38734};
    private int arrowShaftId = 52;
    private int bowStringUnstrungId = 54;
    private static final Random rand = new Random();
    @Override
    public void poll() {
        final State state = getState();
        if (state == null) {
            return;
        }
        switch (state) {
            case TELE_BURTHORPE:
                ctx.widgets.component(1477,72).component(1).click();
                sleep(1500);
                ctx.widgets.component(1092,18).click();
                sleep(21000);
                step=1;
                break;
            case CUT_TREES: 
                if(ctx.backpack.select().size()== 28){
                    step=2;
                    break;
                }

                GameObject tree = ctx.objects.select().id(oakTreeId).nearest().poll();
                if (tree.inViewport()) {
                    if (tree.interact("Chop down")) {
                        sleep(5000);
                    }
                } else {
                    ctx.camera.turnTo(tree.tile());
                    ctx.movement.step(tree.tile());
                    sleep(5000);
                }
                break;
            case MAKE_BOWSTRING:
                Item log = ctx.backpack.select().id(logId,oakLogId).poll();
                if (log.interact("Craft")) {
                    sleep(2000);
                    if(ctx.widgets.component(1179,36).component(1).visible()){
                        ctx.widgets.component(1179,36).component(1).click();
                    }
                    sleep(1500);
                    if (!ctx.players.local().inMotion() && ctx.players.local().animation() == -1) {
                        ctx.widgets.component(1371,44).component(5).click();
                        sleep(2000);
                        ctx.widgets.component(1370,20).click();
                        sleep(50000);
                    }
                }
                step=3;
                break;
            case BANK_BURTHORPE:
            	ctx.movement.step(new Tile(2889,3528));
            	sleep(5000);
            	if(!ctx.bank.opened()){
					ctx.bank.open();
					sleep(3000);
				}
				if(rand.nextInt(100) < 95){
					ctx.bank.depositInventory();
					Condition.sleep(rand.nextInt(1000) + 300);
					if(rand.nextInt(100) > 95){
						ctx.bank.close();
						Condition.sleep(rand.nextInt(1000) + 300);
					}
				}
				else{
					ctx.bank.deposit(bowStringUnstrungId, 28);
					Condition.sleep(rand.nextInt(1000) + 300);
					if(rand.nextInt(100) > 85){
						ctx.bank.close();
						Condition.sleep(rand.nextInt(1000) + 300);
					}
				}
                step=1;
                break;
            default:
                break;
           
        }
    }

    private enum State {
        TELE_BURTHORPE, CUT_TREES, MAKE_BOWSTRING, BANK_BURTHORPE  
    }

    private State getState() {
        switch(step){
        case 0:
            return State.TELE_BURTHORPE;
        case 1:
            return State.CUT_TREES;
        case 2:
            return State.MAKE_BOWSTRING;
        case 3:
            return State.BANK_BURTHORPE;
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

