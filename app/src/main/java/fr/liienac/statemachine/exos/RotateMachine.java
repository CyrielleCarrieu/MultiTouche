package fr.liienac.statemachine.exos;

import java.util.TreeMap;

import fr.liienac.statemachine.StateMachine;
import fr.liienac.statemachine.event.Move;
import fr.liienac.statemachine.event.Press;
import fr.liienac.statemachine.event.Release;
import fr.liienac.statemachine.geometry.Point;
import fr.liienac.statemachine.graphic.Item;


public class RotateMachine extends StateMachine {
    Item graphicItem = null;
    TreeMap<Integer, Point> listCursors = new TreeMap<>();

    public RotateMachine(Item graphicItem) {
        this.graphicItem = graphicItem;
    }

    public State start = new State() {
        public Transition press = new Transition<Press>() {
            public boolean guard() {
                return evt.graphicItem == graphicItem && listCursors.size()== 0;
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
                return evt.graphicItem ==graphicItem && listCursors.size() == 1 &&
                        Point.distance(listCursors.firstEntry().getValue(), evt.p) > 50 ;
            }

            public void action() {

            }

            public State goTo() {
                return firstTouched;
            }
        };

        public Transition move2 = new Transition<Move>() {
            public boolean guard() {
                return evt.graphicItem == graphicItem && listCursors.size() > 1 &&
                        Point.distance(listCursors.firstEntry().getValue(), evt.p) > 50 ;
            }

            public void action() {

            }

            public State goTo() {
                return secondTouched;
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
                return firstTouched;
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

    public State firstTouched = new State() {

        public Transition press = new Transition<Press>() {
            public boolean guard() {
                return evt.graphicItem == graphicItem && listCursors.size() == 1;
            }

            public void action() {
                listCursors.put(evt.cursorID, evt.p);
            }

            public State goTo() {
                return secondTouched;
            }
        };

        public Transition move = new Transition<Move>() {
            public boolean guard() {
                return evt.cursorID == listCursors.firstKey() && listCursors.size() == 1;
            }
            public void action() {
                Point p = listCursors.firstEntry().getValue();
                graphicItem.translateBy(new fr.liienac.statemachine.geometry.Vector(p, evt.p));
                listCursors.put(listCursors.firstKey(), evt.p);
            }

            public State goTo() {
                return firstTouched;
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

    public State secondTouched = new State() {

        public Transition press = new Transition<Press>() {
            public boolean guard() {
                return evt.graphicItem == graphicItem ;
            }

            public void action() {
                graphicItem.style.r = 255;
                listCursors.put(evt.cursorID, evt.p);
            }

            public State goTo() {
                return secondTouched;
            }
        };

        public Transition resizeP0 = new Transition<Move>() {
            public boolean guard() {
                return listCursors.containsKey((Object)evt.cursorID) &&
                        listCursors.lastKey() == evt.cursorID && listCursors.size() >= 2 ;
            }
            public void action() {
                Point p0 = listCursors.firstEntry().getValue();
                Point p1 = listCursors.lastEntry().getValue();
                graphicItem.rotateBy(new fr.liienac.statemachine.geometry.Vector(p0, evt.p), new fr.liienac.statemachine.geometry.Vector(p0, p1), p0);
                listCursors.put(listCursors.lastKey(), evt.p);
            }

            public State goTo() {
                return secondTouched;
            }
        };

        public Transition resizeP1 = new Transition<Move>() {
            public boolean guard() {
                return listCursors.containsKey((Object)evt.cursorID) &&
                        listCursors.firstKey() == evt.cursorID && listCursors.size() >= 2 ;
            }
            public void action() {
                Point p0 = listCursors.firstEntry().getValue();
                Point p1 = listCursors.lastEntry().getValue();
                graphicItem.rotateBy(new fr.liienac.statemachine.geometry.Vector(p1, evt.p), new fr.liienac.statemachine.geometry.Vector(p1, p0), p1);
                listCursors.put(listCursors.firstKey(), evt.p);
            }

            public State goTo() {
                return secondTouched;
            }
        };

        public Transition release = new Transition<Release>() {
            public boolean guard() {
                return listCursors.containsKey((Object)evt.cursorID) && listCursors.size() > 2 ;
            }
            public void action() {
                listCursors.remove((Object)evt.cursorID);
            }

            public State goTo() {
                return secondTouched;
            }
        };

        public Transition release2 = new Transition<Release>() {
            public boolean guard() {
                return listCursors.containsKey((Object)evt.cursorID) && listCursors.size() == 2 ;
            }
            public void action() {
                listCursors.remove((Object)evt.cursorID);
            }

            public State goTo() {
                return firstTouched;
            }
        };

    };
}