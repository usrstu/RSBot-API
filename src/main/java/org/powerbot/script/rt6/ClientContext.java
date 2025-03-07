package org.powerbot.script.rt6;

import org.powerbot.bot.rt6.*;
import org.powerbot.bot.rt6.client.Client;
import org.powerbot.script.Bot;
import org.powerbot.script.*;

import java.util.List;

/**
 * ClientContext
 */
public class ClientContext extends org.powerbot.script.ClientContext<Client> {
	public final CombatBar combatBar;
	public final Bank bank;
	public final Camera camera;
	public final Chat chat;
	public final DepositBox depositBox;
	public final Equipment equipment;
	public final Game game;
	public final GroundItems groundItems;
	public final HintArrows hintArrows;
	public final Hud hud;
	public final Backpack backpack;
	public final Lobby lobby;
	public final Menu menu;
	public final Movement movement;
	public final Npcs npcs;
	public final Objects objects;
	public final Players players;
	public final Powers powers;
	public final Projectiles projectiles;
	public final Varpbits varpbits;
	public final Skills skills;
	public final Summoning summoning;
	public final Widgets widgets;
	public final ProductionInterface productionInterface;
	public final Components components;

	final Items items;
	final Map map;

	private ClientContext(final Bot<ClientContext> bot) {
		super(bot);

		if (controller != null) {
			final List<Class<? extends Script>> d = controller.daemons();
			d.addAll(bot.listDaemons());
			d.add(WidgetCloser.class);
			d.add(TicketDestroy.class);
			d.add(BankPin.class);
			d.add(Antipattern.class);
		}

		combatBar = new CombatBar(this);
		backpack = new Backpack(this);
		bank = new Bank(this);
		camera = new Camera(this);
		chat = new Chat(this);
		depositBox = new DepositBox(this);
		equipment = new Equipment(this);
		game = new Game(this);
		groundItems = new GroundItems(this);
		hintArrows = new HintArrows(this);
		hud = new Hud(this);
		lobby = new Lobby(this);
		menu = new Menu(this);
		movement = new Movement(this);
		npcs = new Npcs(this);
		objects = new Objects(this);
		players = new Players(this);
		powers = new Powers(this);
		projectiles = new Projectiles(this);
		varpbits = new Varpbits(this);
		skills = new Skills(this);
		summoning = new Summoning(this);
		widgets = new Widgets(this);
		productionInterface = new ProductionInterface(this);
		components = new Components(this);

		items = new Items(this);
		map = new Map(this);
	}

	/**
	 * Creates a new chained context.
	 *
	 * @param ctx the parent context
	 */
	public ClientContext(final ClientContext ctx) {
		super(ctx);
		combatBar = ctx.combatBar;
		backpack = ctx.backpack;
		bank = ctx.bank;
		camera = ctx.camera;
		chat = ctx.chat;
		depositBox = ctx.depositBox;
		equipment = ctx.equipment;
		game = ctx.game;
		groundItems = ctx.groundItems;
		hintArrows = ctx.hintArrows;
		hud = ctx.hud;
		lobby = ctx.lobby;
		menu = ctx.menu;
		movement = ctx.movement;
		npcs = ctx.npcs;
		objects = ctx.objects;
		players = ctx.players;
		powers = ctx.powers;
		projectiles = ctx.projectiles;
		varpbits = ctx.varpbits;
		skills = ctx.skills;
		summoning = ctx.summoning;
		widgets = ctx.widgets;
		productionInterface = ctx.productionInterface;
		components = ctx.components;

		items = ctx.items;
		map = ctx.map;
	}

	/**
	 * Creates a new context for the given {@link org.powerbot.script.Bot}.
	 *
	 * @param bot the bot to associate with
	 * @return a new context
	 */
	public static ClientContext newContext(final Bot<ClientContext> bot) {
		return new ClientContext(bot);
	}
}
