package scripts;
import org.powerbot.bot.rt6.client.Npc;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt6.ClientContext;

@Script.Manifest(name="AutoFighter", description="Fights Selected Enemies")

public class Start extends PollingScript<ClientContext>{
	
	private Npc target;
	
	private enum State{
		SEARCHING, FIGHTING, HEALING
	}
	
	@Override
	public void start(){
		System.out.println("Loaded in start");
	}

	@Override
	public void poll() {
		System.out.println("Loaded");
	}
	
}
