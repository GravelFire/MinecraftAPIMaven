package vsauko.mineplayapi.api.protocollib.packet.scoreboard;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import vsauko.mineplayapi.api.protocollib.packet.AbstractPacket;

import java.util.Collection;
import java.util.List;
import vsauko.mineplayapi.api.protocollib.packet.enums.IntEnum;

public class WrapperPlayServerScoreboardTeam extends AbstractPacket {

	public static final PacketType TYPE =
			PacketType.Play.Server.SCOREBOARD_TEAM;

	public WrapperPlayServerScoreboardTeam() {
		super(new PacketContainer(TYPE), TYPE);
		handle.getModifier().writeDefaults();
	}

	public WrapperPlayServerScoreboardTeam(PacketContainer packet) {
		super(packet, TYPE);
	}

	/**
	 * Enum containing all known modes.
	 *
	 * @author dmulloy2
	 */
	public static class Mode extends IntEnum {

		public static final int TEAM_CREATED = 0;
		public static final int TEAM_REMOVED = 1;
		public static final int TEAM_UPDATED = 2;
		public static final int PLAYERS_ADDED = 3;
		public static final int PLAYERS_REMOVED = 4;

		private static final Mode INSTANCE = new Mode();


		public static Mode getInstance() {
			return INSTANCE;
		}
	}

	/**
	 * Retrieve Team Name.
	 * <p>
	 * Notes: a unique name for the team. (Shared with scoreboard).
	 *
	 * @return The current Team Name
	 */
	public String getName() {
		return handle.getStrings().read(0);
	}

	/**
	 * Set Team Name.
	 *
	 * @param value - new value.
	 */
	public void setName(String value) {
		handle.getStrings().write(0, value);
	}

	/**
	 * Retrieve Team Display Name.
	 * <p>
	 * Notes: only if Mode = 0 or 2.
	 *
	 * @return The current Team Display Name
	 */
	public String getDisplayName() {
		return handle.getStrings().read(1);
	}

	/**
	 * Set Team Display Name.
	 *
	 * @param value - new value.
	 */
	public void setDisplayName(String value) {
		handle.getStrings().write(1, value);
	}

	/**
	 * Retrieve Team Prefix.
	 * <p>
	 * Notes: only if Mode = 0 or 2. Displayed before the players' name that are
	 * part of this team.
	 *
	 * @return The current Team Prefix
	 */
	public String getPrefix() {
		return handle.getStrings().read(2);
	}

	/**
	 * Set Team Prefix.
	 *
	 * @param value - new value.
	 */
	public void setPrefix(String value) {
		handle.getStrings().write(2, value);
	}

	/**
	 * Retrieve Team Suffix.
	 * <p>
	 * Notes: only if Mode = 0 or 2. Displayed after the players' name that are
	 * part of this team.
	 *
	 * @return The current Team Suffix
	 */
	public String getSuffix() {
		return handle.getStrings().read(3);
	}

	/**
	 * Set Team Suffix.
	 *
	 * @param value - new value.
	 */
	public void setSuffix(String value) {
		handle.getStrings().write(3, value);
	}

	/**
	 * Retrieve Name Tag Visibility.
	 * <p>
	 * Notes: only if Mode = 0 or 2. always, hideForOtherTeams, hideForOwnTeam,
	 * never.
	 *
	 * @return The current Name Tag Visibility
	 */
	public String getNameTagVisibility() {
		return handle.getStrings().read(4);
	}

	/**
	 * Set Name Tag Visibility.
	 *
	 * @param value - new value.
	 */
	public void setNameTagVisibility(String value) {
		handle.getStrings().write(4, value);
	}

	/**
	 * Retrieve Color.
	 * <p>
	 * Notes: only if Mode = 0 or 2. Same as Chat colors.
	 *
	 * @return The current Color
	 */
	public int getColor() {
		return handle.getIntegers().read(0);
	}

	/**
	 * Set Color.
	 *
	 * @param value - new value.
	 */
	public void setColor(int value) {
		handle.getIntegers().write(0, value);
	}

	/**
	 * Get the collision rule.
	 * Notes: only if Mode = 0 or 2. always, pushOtherTeams, pushOwnTeam, never.
	 * @return The current collision rule
	 */
	public String getCollisionRule() {
		return handle.getStrings().read(5);
	}

	/**
	 * Sets the collision rule.
	 * @param value - new value.
	 */
	public void setCollisionRule(String value) {
		handle.getStrings().write(5, value);
	}

	/**
	 * Retrieve Players.
	 * <p>
	 * Notes: only if Mode = 0 or 3 or 4. Players to be added/remove from the
	 * team. Max 40 characters so may be uuid's later
	 *
	 * @return The current Players
	 */
	public List<String> getPlayers() {
		return (List<String>) handle.getSpecificModifier(Collection.class)
				.read(0);
	}

	/**
	 * Set Players.
	 *
	 * @param value - new value.
	 */
	public void setPlayers(List<String> value) {
		handle.getSpecificModifier(Collection.class).write(0, value);
	}

	/**
	 * Retrieve Mode.
	 * <p>
	 * Notes: if 0 then the team is created. If 1 then the team is removed. If 2
	 * the team team information is updated. If 3 then new players are added to
	 * the team. If 4 then players are removed from the team.
	 *
	 * @return The current Mode
	 */
	public int getMode() {
		return handle.getIntegers().read(1);
	}

	/**
	 * Set Mode.
	 *
	 * @param value - new value.
	 */
	public void setMode(int value) {
		handle.getIntegers().write(1, value);
	}

	/**
	 * Retrieve pack option data. Pack data is calculated as follows:
	 *
	 * <pre>
	 * <code>
	 * int data = 0;
	 * if (team.allowFriendlyFire()) {
	 *     data |= 1;
	 * }
	 * if (team.canSeeFriendlyInvisibles()) {
	 *     data |= 2;
	 * }
	 * </code>
	 * </pre>
	 *
	 * @return The current pack option data
	 */
	public int getPackOptionData() {
		return handle.getIntegers().read(2);
	}

	/**
	 * Set pack option data.
	 *
	 * @param value - new value
	 * @see #getPackOptionData()
	 */
	public void setPackOptionData(int value) {
		handle.getIntegers().write(2, value);
	}
}