package fr.liienac.statemachine.exos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import fr.liienac.statemachine.StateMachine;
import fr.liienac.statemachine.event.Move;
import fr.liienac.statemachine.event.Press;
import fr.liienac.statemachine.event.Release;
import fr.liienac.statemachine.geometry.Point;
import fr.liienac.statemachine.graphic.Item;


public class DNDMachine extends StateMachine {
    Item graphicItem = null;
    TreeMap<Integer, Point> listCursors = new TreeMap<>();

    public DNDMachine(Item graphicItem) {
        this.graphicItem = graphicItem;
    }

    public State start = new State() {
        public Transition press = new Transition<Press>() {
            public boolean guard() {
                return evt.graphicItem == graphicItem && listCursors.size() == 0;
            }

            public void action() {
                graphicItem.style.r = 255;
                listCursors.put(evt.cursorID, evt.p);
            }

            public State goTo() {
                return hysterese;
            }
        };
    };

    public State hysterese = new State() {
        public Transition press = new Transition<Press>() {
            public boolean guard() {
                return evt.graphicItem == graphicItem;
            }

            public void action() {
                listCursors.put(evt.cursorID, evt.p);
            }

            public State goTo() {
                return hysterese;
            }
        };

        public Transition move = new Transition<Move>() {
            public boolean guard() {
                return evt.graphicItem == graphicItem &&
                        Point.distance(listCursors.firstEntry().getValue(), evt.p) > 50 ;
            }

            public void action() {

            }

            public State goTo() {
                return touched;
            }
        };

        public Transition releaseCursor = new Transition<Release>() {
            public boolean guard() {
                return listCursors.containsKey((Object)evt.cursorID) && listCursors.size() > 1 ;
            }
            public void action() {
                listCursors.remove((Object)evt.cursorID);
            }

            public State goTo() {
                return hysterese;
            }
        };

        public Transition releaseLastCursor = new Transition<Release>() {
            public boolean guard() {
                return listCursors.containsKey((Object)evt.cursorID) && listCursors.size() == 1 ;
            }
            public void action() {
                graphicItem.style.r = 0;
                listCursors.remove((Object)evt.cursorID);
            }

            public State goTo() {
                return start;
            }
        };
    };

    public State touched = new State() {

        public Transition press = new Transition<Press>() {
            public boolean guard() {
                return evt.graphicItem == graphicItem;
            }

            public void action() {
                graphicItem.style.r = 255;
                listCursors.put(evt.cursorID, evt.p);
            }

            public State goTo() {
                return touched;
            }
        };

        public Transition move = new Transition<Move>() {
            public boolean guard() {
                return evt.cursorID == listCursors.firstKey();
            }
            public void action() {
                Point p = listCursors.firstEntry().getValue();
                graphicItem.translateBy(new fr.liienac.statemachine.geometry.Vector(p ,evt.p));
                listCursors.put(listCursors.firstKey(), evt.p);
            }

            public State goTo() {
                return touched;
            }
        };

        public Transition releaseCursor = new Transition<Release>() {
            public boolean guard() {
                return listCursors.containsKey((Object)evt.cursorID) && listCursors.size() > 1 ;
            }
            public void action() {
                listCursors.remove((Object)evt.cursorID);
            }

            public State goTo() {
                return touched;
            }
        };

        public Transition releaseLastCursor = new Transition<Release>() {
            public boolean guard() {
                return listCursors.containsKey((Object)evt.cursorID) && listCursors.size() == 1 ;
            }
            public void action() {
                graphicItem.style.r = 0;
                listCursors.remove((Object)evt.cursorID);
            }

            public State goTo() {
                return start;
            }
        };

    };

}