package scripts;
import org.powerbot.script.Condition;
import org.powerbot.script.Filter;
import org.powerbot.script.MessageEvent;
import org.powerbot.script.MessageListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.*;

import java.util.concurrent.Callable;
import java.util.List;
import java.util.Arrays;

@Script.Manifest(name = "Daily Run", description = "Daily Runs")
public class DailyRun extends PollingScript<ClientContext> implements MessageListener {
    private int step = 0;
    @Override
    public void poll() {
        final State state = getState();
        if (state == null) {
            return;
        }
        switch (state) {
            case PORTSARIM:
                ctx.widgets.component(1477,72).component(1).click();
                sleep(1500);
                ctx.widgets.component(1092,18).click();
                sleep(21000);
                
                //go to location tile (3013,3221) not needed too close to teleport
                //click on NPC gerrant
                Npc gerrant = ctx.npcs.select().id(558).nearest().poll();
                System.out.println(gerrant.name());
                Tile[] tile = new Tile[]{new Tile(3014,3223)};
                TilePath pat = new TilePath(ctx, tile);
                pat.traverse();
                sleep(3000);
                //if inViewPort()
                if(!gerrant.inViewport()) {
                    ctx.movement.step(gerrant);
                    ctx.camera.turnTo(gerrant);
                }
                sleep(5000);
                gerrant.interact("trade");
                sleep(3000);
                System.out.println(Arrays.toString(gerrant.actions()));

                //select feathers on opened widget
                if(!ctx.widgets.component(1265,20).component(7).visible()){ break; }

                ctx.widgets.component(1265,20).component(7).click();
                sleep(1000);

                if(ctx.widgets.component(1265,73).valid())
                    ctx.widgets.component(1265,73).click();
                sleep(1000);
                if(ctx.widgets.component(1265,81).valid())
                    ctx.widgets.component(1265,81).click();
                sleep(1000);

                //Buy fire rune from Betty
                Tile[] tilesToBetty = new Tile[]{new Tile(3019,3228), new Tile(3018,3244), new Tile(3019,3258)};
                TilePath pathToBetty = new TilePath(ctx, tilesToBetty);
                for(int i=0;i<3;i++){
                    pathToBetty.traverse();
                    sleep(10000);
                }
                System.out.println(ctx.camera.pitch()+","+ctx.camera.yaw());
                GameObject bettyDoor = ctx.objects.select().id(40108).nearest().poll();
                ctx.camera.angle(96);
                System.out.println(bettyDoor.toString());
                bettyDoor.hover();
                sleep(1000);
                System.out.println(Arrays.toString(bettyDoor.actions()));
                bettyDoor.interact("Open");
                sleep(3000);
                Npc betty = ctx.npcs.select().id(583).nearest().poll();
                if(!betty.inViewport()) {
                    ctx.movement.step(betty);
                    ctx.camera.turnTo(betty);
                }
                betty.interact("trade");
                sleep(5000);

                //select fire rune on opened widget
                ctx.widgets.component(1265,20).component(0).click();
                sleep(1000);

                if(ctx.widgets.component(1265,73).valid())
                    ctx.widgets.component(1265,73).click();
                sleep(1000);
                if(ctx.widgets.component(1265,81).valid())
                    ctx.widgets.component(1265,81).click();
                sleep(1000);
                //select air rune on opened widget
                ctx.widgets.component(1265,20).component(2).click();
                sleep(1000);

                if(ctx.widgets.component(1265,73).valid())
                    ctx.widgets.component(1265,73).click();
                sleep(1000);
                if(ctx.widgets.component(1265,81).valid())
                    ctx.widgets.component(1265,81).click();
                sleep(1000);
 
                step++;
                break;

            case LUMBRIDGE:
                ctx.widgets.component(1477,72).component(1).click();
                sleep(1500);
                ctx.widgets.component(1092,17).click();
                sleep(21000);

                //go to location tile (3013,3221) not needed too close to teleport
                //(3225,3237),(3212,3249),(3196,3252),
                Tile[] tilesToWalk = new Tile[]{new Tile(3225,3237), new Tile(3212,3249), new Tile(3196,3252)};
                TilePath path = new TilePath(ctx, tilesToWalk);
                for(int i=0;i<3;i++){
                    path.traverse();
                    sleep(10000);
                }
                if(ctx.players.local().tile().distanceTo(new Tile(3196,3254,0)) > 10){
                   break;
                }
                System.out.println("Reached Hank");
                //click on NPC hank 
                Npc hank = ctx.npcs.select().id(8864).nearest().poll();
                System.out.println(hank.name());
                //if inViewPort()
                if(!hank.inViewport()) {
                    ctx.movement.step(hank);
                    ctx.camera.turnTo(hank);
                }
                hank.interact("trade");
                sleep(5000);
                System.out.println(Arrays.toString(hank.actions()));

                //select feathers on opened widget
                ctx.widgets.component(1265,20).component(5).click();
                sleep(1000);

                if(ctx.widgets.component(1265,73).valid())
                    ctx.widgets.component(1265,73).click();
                sleep(1000);
                if(ctx.widgets.component(1265,81).valid())
                    ctx.widgets.component(1265,81).click();
                sleep(1000);
                step++;
                break;
            case VARROCK:
                ctx.widgets.component(1477,72).component(1).click();
                sleep(1500);
                ctx.widgets.component(1092,21).click();
                sleep(21000);

                if(!ctx.players.local().tile().equals(new Tile(2899,3544,0))){
                    System.out.println(ctx.players.local().tile().toString());
                    //break;
                }

                //buy runes
                //(3221,3391),(3232,3391),(3243,3401),(3250,3399)
                Tile[] tilesToAubury = new Tile[]{new Tile(3221,3391),new Tile(3232,3391),new Tile(3243,3401),new Tile(3250,3399),new Tile(3253,3398)};
                TilePath pathToAubury = new TilePath(ctx, tilesToAubury);
                for(int i=0;i<5;i++){
                    System.out.println("Traversing path");
                    pathToAubury.traverse();
                    sleep(6500);
                }
                System.out.println(ctx.camera.pitch()+","+ctx.camera.yaw());
                GameObject door = ctx.objects.select().id(24381).nearest().poll();
                ctx.camera.angle(158);
                System.out.println(door.toString());
                door.hover();
                sleep(1000);
                System.out.println(Arrays.toString(door.actions()));
                door.interact("Open");
                sleep(3000);
                Npc aubury = ctx.npcs.select().id(5913).nearest().poll();
                if(!aubury.inViewport()) {
                    ctx.movement.step(aubury);
                    ctx.camera.turnTo(aubury);
                }
                aubury.interact("trade");
                sleep(5000);

                //select fire rune on opened widget
                ctx.widgets.component(1265,20).component(0).click();
                sleep(1000);

                if(ctx.widgets.component(1265,73).valid())
                    ctx.widgets.component(1265,73).click();
                sleep(1000);
                if(ctx.widgets.component(1265,81).valid())
                    ctx.widgets.component(1265,81).click();
                sleep(1000);

                //select air rune on opened widget
                ctx.widgets.component(1265,20).component(2).click();
                sleep(1000);

                if(ctx.widgets.component(1265,73).valid())
                    ctx.widgets.component(1265,73).click();
                sleep(1000);
                if(ctx.widgets.component(1265,81).valid())
                    ctx.widgets.component(1265,81).click();
                sleep(1000);
                step++;
                break;
            case BURTHORPE:
                ctx.widgets.component(1477,72).component(1).click();
                sleep(1500);
                ctx.widgets.component(1092,12).click();
                sleep(21000);
                //(2899,3544)
                if(!ctx.players.local().tile().equals(new Tile(2899,3544,0))){
                    System.out.println(ctx.players.local().tile().toString());
                    break;
                }
                //buy runes
                //(3221,3391),(3232,3391),(3243,3401),(3250,3399)
                Tile[] tilesToCarwen = new Tile[]{new Tile(2913,3547),new Tile(2923,3552)};
                TilePath pathToCarwen = new TilePath(ctx, tilesToCarwen);
                for(int i=0;i<3;i++){
                    System.out.println("Traversing path");
                    pathToCarwen.traverse();
                    sleep(6500);
                }
                Npc carwen = ctx.npcs.select().id(14906).nearest().poll();
                if(!carwen.inViewport()) {
                    ctx.movement.step(carwen);
                    ctx.camera.turnTo(carwen);
                }
                carwen.interact("trade");
                sleep(5000);
                System.out.println("Opened trade with Carwen");
                //select fire rune on opened widget
                ctx.widgets.component(1265,20).component(0).click();
                sleep(1000);

                if(ctx.widgets.component(1265,73).valid())
                    ctx.widgets.component(1265,73).click();
                sleep(1000);
                if(ctx.widgets.component(1265,81).valid())
                    ctx.widgets.component(1265,81).click();
                sleep(1000);
                System.out.println("Bought firerunes");

                //select air rune on opened widget
                ctx.widgets.component(1265,20).component(2).click();
                sleep(1000);

                if(ctx.widgets.component(1265,73).valid())
                    ctx.widgets.component(1265,73).click();
                sleep(1000);
                if(ctx.widgets.component(1265,81).valid())
                    ctx.widgets.component(1265,81).click();
                sleep(1000);
                System.out.println("Bought Air runes");

                step = 0;
                break;
            default:
                break;
           
        }
    }

    public void teleport(String destination){
        ctx.widgets.component(1477,72).component(1).click();
        sleep(1500);
        if("burthorpe".equalsIgnoreCase(destination)){
            ctx.widgets.component(1092,12).click();
        }else if("portsarim".equalsIgnoreCase(destination)){
            ctx.widgets.component(1092,18).click();
        }else if("portsarim".equalsIgnoreCase(destination)){
            ctx.widgets.component(1092,18).click();
        }else if("taverly".equalsIgnoreCase(destination)){
            ctx.widgets.component(1092,18).click();
        }else if("varrock".equalsIgnoreCase(destination)){
            ctx.widgets.component(1092,18).click();
        }else if("lumbridge".equalsIgnoreCase(destination)){
            ctx.widgets.component(1092,18).click();
        }else if("draynor".equalsIgnoreCase(destination)){
            ctx.widgets.component(1092,18).click();
        }else if("alkharid".equalsIgnoreCase(destination)){
            ctx.widgets.component(1092,18).click();
        }else if("edgeville".equalsIgnoreCase(destination)){
            ctx.widgets.component(1092,18).click();
        }else if("falador".equalsIgnoreCase(destination)){
            ctx.widgets.component(1092,18).click();
        }

        sleep(21000);
    }
 
    private enum State {
        PORTSARIM,LUMBRIDGE,VARROCK,BURTHORPE 
    }

    private State getState() {
        switch(step){
        case 0:
            return State.PORTSARIM;
        case 1:
            return State.LUMBRIDGE;
        case 2:
            return State.VARROCK;
        case 3:
            return State.BURTHORPE;
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

