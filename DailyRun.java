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
    private Tile[] tile = null;
    private TilePath tilePath = null;
    private Npc npc = null;
    
    @Override
    public void poll() {
        final State state = getState();
        if (state == null) {
            return;
        }
        switch (state) {
            case PORTSARIM:
                teleport("portsarim");
                
                //go to location tile (3013,3221) not needed too close to teleport
                traversePath(new int[][]{{3014,3223}});

                //click on NPC gerrant
                interactNpc(558,"trade");

                //select feathers on opened widget
                if(!ctx.widgets.component(1265,20).component(7).visible()){ break; }

                ctx.widgets.component(1265,20).component(7).click();
                sleep(1000);
                buyAll();

                //Buy fire rune from Betty
                traversePath(new int[][]{{3019,3228},{3018,3244},{3019,3258}});

                System.out.println(ctx.camera.pitch()+","+ctx.camera.yaw());
                GameObject bettyDoor = ctx.objects.select().id(40108).nearest().poll();
                ctx.camera.angle(96);
                System.out.println(bettyDoor.toString());
                bettyDoor.hover();
                sleep(1000);
                System.out.println(Arrays.toString(bettyDoor.actions()));
                bettyDoor.interact("Open");
                sleep(3000);
                
                interactNpc(583,"trade");

                //select fire rune on opened widget
                ctx.widgets.component(1265,20).component(0).click();
                sleep(1000);
                buyAll();

                //select air rune on opened widget
                ctx.widgets.component(1265,20).component(2).click();
                sleep(1000);
                buyAll();
 
                step++;
                break;

            case LUMBRIDGE:
                teleport("lumbridge");

                //go to location tile (3013,3221) not needed too close to teleport
                traversePath(new int[][]{{3227,3233},{3219,3245},{3206,3248},{3195,3252}});

                System.out.println("Reached Hank");
                //click on NPC hank 
                interactNpc(8864,"trade");

                //select feathers on opened widget
                ctx.widgets.component(1265,20).component(5).click();
                sleep(1000);
                buyAll();

                System.out.println("Money in pouch : "+ctx.backpack.moneyPouchCount());
                step++;
                break;
            case VARROCK:
                teleport("varrock");

                if(!ctx.players.local().tile().equals(new Tile(3214, 3376, 0))){
                    System.out.println(ctx.players.local().tile().toString());
                    break;
                }

                //buy runes
                //(3221,3391),(3232,3391),(3243,3401),(3250,3399)
                traversePath(new int[][]{{3221,3391},{3232,3391},{3243,3401},{3250,3399}});

                System.out.println(ctx.camera.pitch()+","+ctx.camera.yaw());
                GameObject door = ctx.objects.select().id(24381).nearest().poll();
                ctx.camera.angle(158);
                System.out.println(door.toString());
                door.hover();
                sleep(1000);
                System.out.println(Arrays.toString(door.actions()));
                door.interact("Open");
                sleep(3000);
                interactNpc(5913,"trade");

                //select fire rune on opened widget
                ctx.widgets.component(1265,20).component(0).click();
                sleep(1000);
                buyAll();

                //select air rune on opened widget
                ctx.widgets.component(1265,20).component(2).click();
                sleep(1000);
                buyAll();

                step++;
                break;
            case BURTHORPE:
                teleport("burthorpe");

                //(2899,3544)
                if(!ctx.players.local().tile().equals(new Tile(2899,3544,0))){
                    System.out.println(ctx.players.local().tile().toString());
                    break;
                }
                //buy runes
                traversePath(new int[][]{{2913,3547},{2923,3552}});
                interactNpc(14906,"trade");

                //select fire rune on opened widget
                ctx.widgets.component(1265,20).component(0).click();
                sleep(1000);

                buyAll();
                System.out.println("Bought firerunes");

                //select air rune on opened widget
                ctx.widgets.component(1265,20).component(2).click();
                sleep(1000);

                buyAll();
                System.out.println("Bought Air runes");

                step = 0;
                break;
            default:
                break;
           
        }
    }

    private void teleport(String destination){
        do{
            ctx.widgets.component(1477,72).component(1).click();
        }while(!ctx.widgets.component(1092,12).visible());
        sleep(1500);

        if("burthorpe".equalsIgnoreCase(destination)){
            ctx.widgets.component(1092,12).click();
        }else if("portsarim".equalsIgnoreCase(destination)){
            ctx.widgets.component(1092,18).click();
        }else if("taverly".equalsIgnoreCase(destination)){
            ctx.widgets.component(1092,20).click();
        }else if("varrock".equalsIgnoreCase(destination)){
            ctx.widgets.component(1092,21).click();
        }else if("lumbridge".equalsIgnoreCase(destination)){
            ctx.widgets.component(1092,17).click();
        }else if("draynor".equalsIgnoreCase(destination)){
            ctx.widgets.component(1092,14).click();
        }else if("alkharid".equalsIgnoreCase(destination)){
            ctx.widgets.component(1092,10).click();
        }else if("edgeville".equalsIgnoreCase(destination)){
            ctx.widgets.component(1092,15).click();
        }else if("falador".equalsIgnoreCase(destination)){
            ctx.widgets.component(1092,16).click();
        }
        sleep(21000);
        System.out.println("Teleported to : "+destination);
    }

    private void interactNpc(int id, String action){
        Npc npc = ctx.npcs.select().id(id).nearest().poll();
        System.out.println(npc.name());
        //if inViewPort()
        if(!npc.inViewport()) {
            ctx.movement.step(npc);
            ctx.camera.turnTo(npc);
        }
        sleep(5000);
        npc.interact(action);
        sleep(3000);
        System.out.println(Arrays.toString(npc.actions()));
    }
    
    private void buyAll(){
        if(ctx.widgets.component(1265,73).valid())
            ctx.widgets.component(1265,73).click();
        sleep(1000);
        if(ctx.widgets.component(1265,81).valid())
            ctx.widgets.component(1265,81).click();
        sleep(1000);
    }
    
    private void traversePath(int[][] tilesXY){
        Tile[] tiles = new Tile[tilesXY.length];
        int i=0;
    	for(;i<tilesXY[0].length;i++){
    		tiles[i] = new Tile(tilesXY[i][0],tilesXY[i][1]);
    	}
        TilePath path = new TilePath(ctx, tiles);
        for(int i=0;i<3;i++){
            path.traverse();
        	sleep(2000);
            if(ctx.players.local().inMotion()){
            	sleep(5000);
            }
        }
        if(ctx.players.local().tile().distanceTo(new Tile(tilesXY[i-1][0],tilesXY[i-1][1],0)) > 10){
            return false;
        }
        return true;
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

