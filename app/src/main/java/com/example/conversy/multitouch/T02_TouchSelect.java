/*
 * Copyright (c) 2016 Stéphane Conversy - ENAC - All rights Reserved
 */

package com.example.conversy.multitouch;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import fr.liienac.statemachine.event.Move;
import fr.liienac.statemachine.event.PositionalEvent;
import fr.liienac.statemachine.ColorPicking;
import fr.liienac.statemachine.event.Press;
import fr.liienac.statemachine.event.Release;
import fr.liienac.statemachine.event.Timeout;
import fr.liienac.statemachine.exos.ResizeMachine;
import fr.liienac.statemachine.exos.SelectMachine;
import fr.liienac.statemachine.graphic.Item;
import fr.liienac.statemachine.geometry.Point;
import fr.liienac.statemachine.StateMachine;


import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;


public class T02_TouchSelect extends Activity {

	/**
	 * ATTENTION: This was auto-generated to implement the App Indexing API.
	 * See https://g.co/AppIndexing/AndroidStudio for more information.
	 */
//	private GoogleApiClient client;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_main);
		setContentView(new MyView(this));
		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
//		client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.menu_main, menu);
//		return true;
//	}

//	@Override
//	public void onStart() {
//		super.onStart();
//
//		// ATTENTION: This was auto-generated to implement the App Indexing API.
//		// See https://g.co/AppIndexing/AndroidStudio for more information.
//		client.connect();
//		Action viewAction = Action.newAction(
//				Action.TYPE_VIEW, // TODO: choose an action type.
//				"T02_TouchSelect Page", // TODO: Define a title for the content shown.
//				// TODO: If you have web page content that matches this app activity's content,
//				// make sure this auto-generated web page URL is correct.
//				// Otherwise, set the URL to null.
//				Uri.parse("http://host/path"),
//				// TODO: Make sure this auto-generated app deep link URI is correct.
//				Uri.parse("android-app://com.example.conversy.multitouch/http/host/path")
//		);
//		AppIndex.AppIndexApi.start(client, viewAction);
//	}

//	@Override
//	public void onStop() {
//		super.onStop();
//
//		// ATTENTION: This was auto-generated to implement the App Indexing API.
//		// See https://g.co/AppIndexing/AndroidStudio for more information.
//		Action viewAction = Action.newAction(
//				Action.TYPE_VIEW, // TODO: choose an action type.
//				"T02_TouchSelect Page", // TODO: Define a title for the content shown.
//				// TODO: If you have web page content that matches this app activity's content,
//				// make sure this auto-generated web page URL is correct.
//				// Otherwise, set the URL to null.
//				Uri.parse("http://host/path"),
//				// TODO: Make sure this auto-generated app deep link URI is correct.
//				Uri.parse("android-app://com.example.conversy.multitouch/http/host/path")
//		);
//		AppIndex.AppIndexApi.end(client, viewAction);
//		client.disconnect();
//	}

	public class MyView extends View {

		class Cursor {
			public Point p;
			public long id;
			int r, g, b;
		}

		Map<Long, Cursor> cursors = new HashMap<Long, Cursor>();
		Collection<Item> sceneGraph = new Vector<Item>();
		ColorPicking colorPicking = new ColorPicking();

		Handler timeoutHandler = new Handler(Looper.getMainLooper()) {
			public void handleMessage(Message inputMessage) {
				if (inputMessage.what != numTimer) return;
				Timeout evt = new Timeout();
				for (StateMachine m : machines) {
					m.handleEvent(evt);
				}
			}
		};

		int numTimer = 0;

		public void disarmTimer() {
			numTimer += 1;
		}

		public void armTimer(int ms) {
			//Message msg; // dummy msg
			timeoutHandler.sendEmptyMessageDelayed(numTimer, ms);
		}


		Vector<StateMachine> machines = new Vector<StateMachine>();

		public MyView(Context c) {
			super(c);

			// cache paints to avoid recreating them at each draw
			paint = new Paint();
			pickingPaint = new Paint();
			pickingPaint.setAntiAlias(false);

			StateMachine machine;
			Item graphicItem;
			StateMachine machine2;
			Item graphicItem2;
			StateMachine machine3;
			Item graphicItem3;

			graphicItem = new Item(20, 20, 300, 300);
			sceneGraph.add(graphicItem);
			machine = new SelectMachine(graphicItem);
			machines.add(machine);
			graphicItem2 = new Item(500, 500, 300, 300);
			sceneGraph.add(graphicItem2);
			machine2 = new SelectMachine(graphicItem2);
			machines.add(machine2);
			graphicItem3 = new Item(200, 200, 300, 300);
			sceneGraph.add(graphicItem3);
			machine3 = new SelectMachine(graphicItem3);
			machines.add(machine3);

//			machine = new ClickMachine(this);
//			machines.add(machine);
		}

		@Override
		protected void onSizeChanged(int w, int h, int oldw, int oldh) {
			super.onSizeChanged(w, h, oldw, oldh);
			//colorPicking.onSizeChanged(w,h,oldw,oldh);
			colorPicking.onSizeChanged(w, h);
		}

		// cache paints to avoid recreating them at each draw
		Paint paint;
		Paint pickingPaint;

		@Override
		protected void onDraw(Canvas canvas) {

			// "erase" canvas (fill it with white)
			canvas.drawColor(0xFFAAAAAA);
			colorPicking.reset();

			// draw scene graph
			for (Item graphicItem : sceneGraph) {
				// Display view
				canvas.save();
				paint.setARGB(255, graphicItem.style.r, graphicItem.style.g, graphicItem.style.b);
				graphicItem.draw(canvas, paint);
				canvas.restore();

				// Picking view
				//colorPicking.canvas.save();
				//colorPicking.newColor(graphicItem, pickingPaint);
				graphicItem.drawPickingView(colorPicking, pickingPaint);
				//colorPicking.canvas.restore();
			}

			if (false) { // debug: show picking view
				canvas.drawBitmap(colorPicking.bitmap, 0, 0, paint);
			}

			// draw cursors

			for (Map.Entry<Long, Cursor> entry : cursors.entrySet()) {
				Cursor c = entry.getValue();
				paint.setARGB(100, c.r, c.g, c.b);
				canvas.drawCircle(c.p.x, c.p.y, 50, paint);
				canvas.drawText("" + c.id, c.p.x + 30, c.p.y - 30, paint);
			}
		}

		class NewMachine extends StateMachine {
			Item graphicItem = null;
			List<Integer> listCursors = new ArrayList<>();

			Point p0;
			Point p1;
			NewMachine(Item graphicItem) {
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


//-----------------------------------------

		private void onTouchDown(Point p, int cursorid) {
			Item s = (Item) colorPicking.pick(p); //FIXME
			//System.out.println("down "+cursorid + " " + p.x +" "+p.y+ " "+s);
			PositionalEvent<Item> evt = new Press<Item>(cursorid, p, s, 0);
			for (StateMachine m : machines) {
				m.handleEvent(evt);
			}

			Cursor c = new Cursor();
			c.p = p;
			c.id = cursorid;
			c.r = (int) Math.floor(Math.random() * 100);
			c.g = (int) Math.floor(Math.random() * 100);
			c.b = (int) Math.floor(Math.random() * 100);
			cursors.put(Long.valueOf(c.id), c);
			invalidate();
		}

		private void onTouchMove(Point p, int cursorid) {
			//System.out.println("move "+cursorid+ " " +p.x+" "+p.y);
			Cursor c = cursors.get(Long.valueOf(cursorid));

			if (Point.distance(c.p, p) > 0) {
				Item s = (Item) colorPicking.pick(p); //FIXME
				PositionalEvent evt = new Move(cursorid, p, s, 0);
				for (StateMachine m : machines) {
					m.handleEvent(evt);
				}
				c.p = p;
			}

			invalidate();
		}

		private void onTouchUp(Point p, int cursorid) {
			//System.out.println("up "+cursorid);
			Item s = (Item) colorPicking.pick(p); //FIXME
			PositionalEvent evt = new Release(cursorid, p, s, 0);
			for (StateMachine m : machines) {
				m.handleEvent(evt);
			}

			cursors.remove(Long.valueOf(cursorid));
			invalidate();
		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			//System.out.println("------");
			int action = event.getActionMasked(); //MotionEventCompat.getActionMasked(event);
			int index = event.getActionIndex(); //MotionEventCompat.getActionIndex(event);
			int id = event.getPointerId(index); //MotionEventCompat.getPointerId(event, index);
			float x, y;

			switch (action) {
				case MotionEvent.ACTION_DOWN:
				case MotionEvent.ACTION_POINTER_DOWN:
					x = event.getX(index); //MotionEventCompat.getX(event, index);
					y = event.getY(index); //MotionEventCompat.getY(event, index);
					onTouchDown(new Point(x, y), id);
					break;
				case MotionEvent.ACTION_MOVE:
					for (int i = 0; i < event.getPointerCount(); ++i) {
						x = event.getX(i); //MotionEventCompat.getX(event, i);
						y = event.getY(i); //MotionEventCompat.getY(event, i);
						id = event.getPointerId(i); //MotionEventCompat.getPointerId(event, i);
						onTouchMove(new Point(x, y), id);
					}
					break;
				case MotionEvent.ACTION_UP:
				case MotionEvent.ACTION_POINTER_UP:
					x = event.getX(index); //MotionEventCompat.getX(event, index);
					y = event.getY(index); //MotionEventCompat.getY(event, index);
					onTouchUp(new Point(x, y), id);
					break;
			}

			return true;
		}

		@Override
		public boolean onHoverEvent(MotionEvent event) {
			//System.out.println("------");
			int action = event.getActionMasked(); //MotionEventCompat.getActionMasked(event);
			int index = event.getActionIndex(); //MotionEventCompat.getActionIndex(event);
			int id = event.getPointerId(index); //MotionEventCompat.getPointerId(event, index);
			float x, y;

			switch (action) {
				case MotionEvent.ACTION_HOVER_ENTER:
					x = event.getX(index); //MotionEventCompat.getX(event, index);
					y = event.getY(index); //MotionEventCompat.getY(event, index);
					onTouchDown(new Point(x, y), id);
					break;
				case MotionEvent.ACTION_HOVER_MOVE:
					for (int i = 0; i < event.getPointerCount(); ++i) {
						x = event.getX(i); //MotionEventCompat.getX(event, i);
						y = event.getY(i); //MotionEventCompat.getY(event, i);
						id = event.getPointerId(i); //MotionEventCompat.getPointerId(event, i);
						onTouchMove(new Point(x, y), id);
					}
					break;
				case MotionEvent.ACTION_HOVER_EXIT:
					x = event.getX(index); //MotionEventCompat.getX(event, index);
					y = event.getY(index); //MotionEventCompat.getY(event, index);
					onTouchUp(new Point(x, y), id);
					break;
			}

			return true;
		}
/*
		@Override
		public boolean onHoverEvent(MotionEvent event) {
			//System.out.println("=> TouchEvent");
			//System.out.println("---");
			int action = MotionEventCompat.getActionMasked(event);
			int index = MotionEventCompat.getActionIndex(event);
			int id = MotionEventCompat.getPointerId(event, index);
			float x,y;

			switch (action) {
			case MotionEvent.ACTION_HOVER_ENTER:
				x = MotionEventCompat.getX(event, index);
				y = MotionEventCompat.getY(event, index);
				onTouchDown(new Point(x,y),id);
				break;
			case MotionEvent.ACTION_HOVER_MOVE:
				//x = MotionEventCompat.getX(event, index);
				//y = MotionEventCompat.getY(event, index);
				//onTouchMove(new Point(x,y),id);
				for (int i=0; i<event.getPointerCount(); ++i) {
					x = MotionEventCompat.getX(event, i);
					y = MotionEventCompat.getY(event, i);
					id = MotionEventCompat.getPointerId(event, i);
					// event sent though there may be no move for that particular touch
					//System.out.println("------MoveEvent");
					//x=(float)Math.floor(x/15)*15;
					//y=(float)Math.floor(y/15)*15;
					onTouchMove(new Point(x,y),id);
				}
				break;
			case MotionEvent.ACTION_HOVER_EXIT:
				x = MotionEventCompat.getX(event, index);
				y = MotionEventCompat.getY(event, index);
				onTouchUp(new Point(x,y),id);
				break;
			}
			//System.out.println("<= TouchEvent");
			return true;
		}*/

	}
}


