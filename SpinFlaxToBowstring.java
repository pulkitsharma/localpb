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

@Script.Manifest(name = "Fletch Bowstrings", description = "Fletch Bowstrings")
public class SpinFlaxToBowstring extends PollingScript<ClientContext> implements MessageListener {
    private int step = 1;
    private int cutFlaxId = 1779;
    private int[] growingFlaxIds = {67263, 67264, 67265};
    private int spinningWheel = 66850;
    private int bowStringId = 1777;

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
            case REACH_FLAX_FIELD:
            	ctx.movement.step(new Tile(2890,3518));
            	sleep(5000);
            	ctx.movement.step(new Tile(2891,3504));
            	sleep(5000);
            	ctx.movement.step(new Tile(2891,3488));
            	sleep(5000);
            	ctx.movement.step(new Tile(2888,3472));
            	sleep(5000);
            	step=2;
            	break;
            case PICK_FLAX: 
                if(ctx.backpack.select().size()== 28){
                    step=3;
                    break;
                }
                GameObject flax = ctx.objects.select().id(growingFlaxIds).nearest().poll();
                if (flax.inViewport()) {
                    if (flax.interact("Pick")) {
                        sleep(5000);
                    }
                } else {
                    ctx.camera.turnTo(flax.tile());
                    ctx.movement.step(flax.tile());
                    sleep(5000);
                }
                break;
            case SPIN_FLAX:
            	ctx.movement.step(new Tile(2888,3475));
            	sleep(5000);
            	ctx.movement.step(new Tile(2891,3488));
            	sleep(5000);
                GameObject spinningWheelObject = ctx.objects.select().id(spinningWheel).nearest().poll();
                if (spinningWheelObject.interact("Spin")) {
                    sleep(2500);
                    ctx.widgets.component(1371,44).component(5).click();
                    sleep(2000);
                    ctx.widgets.component(1370,20).click();
                    sleep(30000);
                }
                step=4;
                break;
            case BANK_BURTHORPE:
            	ctx.movement.step(new Tile(2889,3513));
            	sleep(5000);
            	ctx.movement.step(new Tile(2889,3528));
            	sleep(5000);
            	if(!ctx.bank.opened()){
					ctx.bank.open();
					sleep(3000);
				}
				if(rand.nextInt(100) < 95){
					ctx.bank.depositInventory();
					Condition.sleep(rand.nextInt(1000) + 300);
				}
				else{
					ctx.bank.deposit(bowStringId, 28);
					Condition.sleep(rand.nextInt(1000) + 300);
				}
				ctx.bank.close();
                step=1;
                break;
            default:
                break;
           
        }
    }

    private enum State {
        TELE_BURTHORPE,REACH_FLAX_FIELD, PICK_FLAX, SPIN_FLAX, BANK_BURTHORPE  
    }

    private State getState() {
        switch(step){
        case 0:
            return State.TELE_BURTHORPE;
        case 1:
            return State.REACH_FLAX_FIELD;
        case 2:
            return State.PICK_FLAX;
        case 3:
            return State.SPIN_FLAX;
        case 4:
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

