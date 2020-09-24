package fr.liienac.statemachine.exos;

import java.util.ArrayList;
import java.util.List;

import fr.liienac.statemachine.StateMachine;
import fr.liienac.statemachine.event.Move;
import fr.liienac.statemachine.event.Press;
import fr.liienac.statemachine.event.Release;
import fr.liienac.statemachine.geometry.Point;
import fr.liienac.statemachine.graphic.Item;


public class ResizeMachine extends StateMachine {
    Item graphicItem = null;
    List<Integer> listCursors = new ArrayList<>();

    Point p0;
    Point p1;
    public ResizeMachine(Item graphicItem) {
        this.graphicItem = graphicItem;
    }

    public State start = new State() {
        Transition press = new Transition<Press>() {
            public boolean guard() {
                return evt.graphicItem == graphicItem && listCursors.size()== 0;
            }

            public void action() {
                graphicItem.style.r = 255;
                listCursors.add(evt.cursorID);
                p0 = evt.p;
            }

            public State goTo() {
                return hysterese;
            }
        };
    };

    public State hysterese = new State() {
        Transition move = new Transition<Move>() {
            public boolean guard() {
                return evt.graphicItem == graphicItem && Point.distance(p0,evt.p)>100 ;
            }

            public void action() {
            }

            public State goTo() {
                return firstTouched;
            }
        };

        Transition release = new Transition<Release>() {
            public boolean guard() {
                return listCursors.contains(evt.cursorID) && listCursors.size() == 1 ;
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

        Transition press = new Transition<Press>() {
            public boolean guard() {
                return evt.graphicItem == graphicItem && listCursors.size()==1;
            }

            public void action() {
                graphicItem.style.r = 255;
                listCursors.add(evt.cursorID);
                p1 = evt.p;
            }

            public State goTo() {
                return secondTouched;
            }
        };

        Transition move = new Transition<Move>() {
            public boolean guard() {
                return evt.cursorID == listCursors.get(0) && listCursors.size()== 1;
            }
            public void action() {
                graphicItem.translateBy(new fr.liienac.statemachine.geometry.Vector(p0 ,evt.p));
                p0 = evt.p;
            }

            public State goTo() {
                return firstTouched;
            }
        };

        Transition release = new Transition<Release>() {
            public boolean guard() {
                return listCursors.contains(evt.cursorID) && listCursors.size() == 1 ;
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

        Transition press = new Transition<Press>() {
            public boolean guard() {
                return evt.graphicItem == graphicItem ;
            }

            public void action() {
                graphicItem.style.r = 255;
                listCursors.add(evt.cursorID);
                p1 = evt.p;
            }

            public State goTo() {
                return secondTouched;
            }
        };

        Transition resizeP0 = new Transition<Move>() {
            public boolean guard() {
                return listCursors.contains(evt.cursorID) && listCursors.get(1) == evt.cursorID && listCursors.size() >= 2 ;
            }
            public void action() {
                float ds = Point.minus(p0, evt.p).norm()/Point.minus(p0,p1).norm();
                graphicItem.scaleBy(ds, p0);
                p1 = evt.p;
            }

            public State goTo() {
                return secondTouched;
            }
        };

        Transition resizeP1 = new Transition<Move>() {
            public boolean guard() {
                return listCursors.contains(evt.cursorID) && listCursors.get(0) == evt.cursorID && listCursors.size() >= 2 ;
            }
            public void action() {
                float ds = Point.minus(p1,evt.p).norm()/Point.minus(p1,p0).norm();
                graphicItem.scaleBy(ds, p1);
                p0 = evt.p;
            }

            public State goTo() {
                return secondTouched;
            }
        };

        Transition release = new Transition<Release>() {
            public boolean guard() {
                return listCursors.contains(evt.cursorID) && listCursors.size() == 2 ;
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