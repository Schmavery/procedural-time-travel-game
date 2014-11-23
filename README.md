procedural-time-travel-game
===========================

Time travel in a procedurally generated world.  This is an unrealistically ambitious project that involves a large (finite) world where NPC villagers build little towns and defend them against external threats.  This gradual fluctuation of towns can be appreciated by the player due to the fact that the player can time travel.

At certain points (probably at death, but yet to be determined), the player will be transported to a different time and location on the map.  
Imagine you are flung far into the future.  You see the ruins of a once-prosperous town that you used to frequent for supplies.  Maybe it is home to the odd monster or two.  
Perhaps, instead, you are transported to the distant past.  You come across a few scattered NPCs gradually putting together a tiny village.  Will this become the sizeable town you remember?  It's up to you!  Your actions in the past directly affect the world's future!

Explanation of project:
=======================
procedural-time is an attempt to realize this project using [LWJGL](http://www.lwjgl.org/) (OpenGL) for graphics.  It makes use of a couple of external libraries, namely LWJGL and slick-utils.  It also uses fonts generated using the wonderful [BMFont](http://www.angelcode.com/products/bmfont/).

In order to generate spritesheet metadata, this project uses the [Procedural-Time-Travel-Game Sprite Tool](https://github.com/Schmavery/pttg-sprite-tool).

Installation Instructions:
==========================

This project requires some external libraries to be linked in before it will run properly.

[This](http://thecodinguniverse.com/lwjgl-workspace/) link will should you how to link up LWJGL to Eclipse.  Pretty much just follow all the instructions and it should work.

Historical Ramblings:
---------------------
procedural-time-v1.0 was the original attempt at a realization of the project and is no longer a part of this repo.  It basically consisted of some basic procedurally generated terrain, dumb NPCs, useless items (that can be picked up!) and a very basic inventory/UI system. This was abandoned because it uses java2d, which proved to be slow.
