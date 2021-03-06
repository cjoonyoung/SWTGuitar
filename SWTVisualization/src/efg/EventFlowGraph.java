package efg;

import java.io.File;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.xml.sax.SAXException;
import static utils.Preconditions.checkNotNull;
import static utils.Preconditions.checkArg;

/**
 * <p>Sitar's brief idea of an event flow graph according to:
 * http://sourceforge.net/apps/mediawiki/guitar/index.php?title=EFG.</p>
 * 
 * <p>Why not use the one provided in GUITAR?</p>
 * 
 * <p>Easier to build, reduced complexity... and we aren't touching the core, which
 * is constantly changed by every other researcher in the known world.</p>
 * 
 * <p>We want our project in an isolated, happy bubble, free from the hands of as many
 * adversaries as possible.</p>
 * 
 * @author Eric Oliver
 *
 */
public class EventFlowGraph {
	
	private Set<EFGEvent> events;
	private Map<WidgetId, List<EFGEvent>> widgets;
	
	private Map<EFGEvent, Set<Adjacency>> outgoingEdges;
	
	/**
	 * Lots of stuff will be package private. EFGs technically aren't mutable and should
	 * come strictly from the parser as far as our visualization is concerned.
	 * Therefore, the constructor should not be publicly available, but I want the parser
	 * et al to access all stuff for the EFG explicitly. package-private it is!
	 */
	EventFlowGraph() {
		init();
	}
	
	private void init() {
		this.events = new HashSet<EFGEvent>();
		this.outgoingEdges = new HashMap<EFGEvent, Set<Adjacency>>();
		this.widgets = new HashMap<WidgetId, List<EFGEvent>>();
	}
	
	void addEvent(EFGEvent event) {
		// update events
		this.events.add(event);
		
		// update widgets
		WidgetId widgetId = event.getWidgetId();
		
		List<EFGEvent> eventsAssocWithWidget = this.widgets.get(widgetId);
		if (eventsAssocWithWidget == null) {
			eventsAssocWithWidget = new ArrayList<EFGEvent>();
			this.widgets.put(widgetId, eventsAssocWithWidget);
		}
		
		eventsAssocWithWidget.add(event);
	}
	
	void addAdjacency(EFGEvent a, EdgeType edge, EFGEvent b) {
		
		checkArg(this.events.contains(a), "a is not an event in this graph");
		checkArg(this.events.contains(b), "b is not an event in this graph");
		checkNotNull(edge, "edge cannot be null");
		
		Adjacency adj = new Adjacency(b, edge);
		
		Set<Adjacency> adjacencies = this.outgoingEdges.get(a);
		if (adjacencies == null) {
			adjacencies = new HashSet<Adjacency>();
			this.outgoingEdges.put(a, adjacencies);
		}
		
		adjacencies.add(adj);
	}
	
	public boolean opensWindow(WidgetId widgetId) {
		List<EFGEvent> events = this.widgets.get(widgetId);
		
		for (EFGEvent event : events) {
			switch(event.getEventType()) {
			case UNRESTRICTED_FOCUS:
			case RESTRICTED_FOCUS:
			case EXPAND:
				return true;
			default:
				
			}
		}
		
		return false;
	}
	
	public Map<EdgeType, Set<WidgetId>> getFollowingWidgets(WidgetId widgetId) {
		
		EnumMap<EdgeType, Set<WidgetId>> followingWidgets = new EnumMap<EdgeType, Set<WidgetId>>(EdgeType.class);
		// initialize the returned adjacencies
		for (EdgeType edgeType : EdgeType.values()) {
			followingWidgets.put(edgeType, new HashSet<WidgetId>());
		}
		
		List<EFGEvent> events = this.widgets.get(widgetId);
		// for each event, get all adjacencies and add them to the set of return values
		for (EFGEvent event : events) {
			for (Adjacency outgoingEdge : this.outgoingEdges.get(event)) {
				followingWidgets.get(outgoingEdge.edgeType).add(outgoingEdge.event.getWidgetId());
			}
		}
		
		return followingWidgets;
	}
	
	public Set<WidgetId> getFollowingWidgets(WidgetId widgetId, EdgeType edgeType) {
		return getFollowingWidgets(widgetId).get(edgeType);
	}
	
	public Set<WidgetId> getAllWidgets() {
		return new HashSet<WidgetId>(this.widgets.keySet());
	}
	
//	private static long transformWidgetId(String widgetId) {
//		if (widgetId.startsWith("w")) { widgetId = widgetId.substring(1, widgetId.length()); }
//		return Long.parseLong(widgetId);
//	}
	
	/**
	 * A convenience method for parsing EFGs. It just calls EFGParser.parseFile()
	 * 
	 * @param efgFile A valid XML file with EFG structure
	 * @return The EFG represented by the file
	 * @throws SAXException if the provided file is malformed
	 */
	public static EventFlowGraph parse(File efgFile) throws SAXException {
		return EFGParser.parseFile(efgFile);
	}
	
	/**
	 * The three edge types as described in the EFG's XSD.
	 * @author Eric Oliver
	 *
	 */
	public enum EdgeType {
		NONE, NORMAL, REACHING;
		
		/**
		 * Given a 0, 1, or 2, will return the correct edge type as described in the EFG's
		 * XSD.
		 * @param num either a 0, 1, or 2
		 * @return The corresponding edge type
		 */
		public static EdgeType parse(int num) {
			switch(num) {
			case 0:
				return NONE;
			case 1:
				return NORMAL;
			case 2:
				return REACHING;
			default:
				throw new IllegalArgumentException(num + " is not a valid edge");
			}
		}
		
		public static EdgeType parse(String num) {
			return parse(Integer.parseInt(num));
		}
	}
	
	/**
	 * A convenience class for representing a tuple and keeping track of silly nulls.
	 * 
	 * To keep nulls away, please do not set the variables here even though you can.
	 * (That's why I said this is a convenience class)
	 * 
	 * @author Eric Oliver
	 *
	 */
	private class Adjacency {
		EFGEvent event;
		EdgeType edgeType;
		
		public Adjacency(EFGEvent event, EdgeType edgeType) {
			checkNotNull(event);
			checkNotNull(edgeType);
			
			this.event = event;
			this.edgeType = edgeType;
		}
		
		@Override public int hashCode() {
			return event.hashCode();
		}
		
		@Override public String toString() {
			return edgeType.toString() + " " + event.toString();
		}
	}
}