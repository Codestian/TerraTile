package com.codestian;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.*;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class TerraTileClient implements ClientModInitializer {

    private static KeyBinding keyBindingNorth;
    private static KeyBinding keyBindingSouth;
    private static KeyBinding keyBindingEast;
    private static KeyBinding keyBindingWest;

    private static KeyBinding keyBindingUp;
    private static KeyBinding keyBindingDown;

    private static KeyBinding keyBindingStart;

    private static KeyBinding keyBindingRotateLeft;
    private static KeyBinding keyBindingRotateRight;

    private static KeyBinding keyBindingScaleUp;
    private static KeyBinding keyBindingScaleDown;

    private static double x = 0;
    private static double y = 30;
    private static double z = 0;

    private static double selectedLatitude = 1.4058351356782655;
    private static double selectedLongitude = 103.9023773594844;

    private static double rotationDegree = -35;

    private static double scale = 153;

    private static boolean isDisplayed = false;

    private Identifier bind1 = new Identifier("terratile", "icon.png");
    private Identifier bind2 = new Identifier("terratile", "icon.png");
    private Identifier bind3 = new Identifier("terratile", "icon.png");

    private Identifier bind4 = new Identifier("terratile", "icon.png");
    private Identifier bind5 = new Identifier("terratile", "icon.png");
    private Identifier bind6 = new Identifier("terratile", "icon.png");

    private Identifier bind7 = new Identifier("terratile", "icon.png");
    private Identifier bind8 = new Identifier("terratile", "icon.png");
    private Identifier bind9 = new Identifier("terratile", "icon.png");

//
    private Identifier bind10 = new Identifier("terratile", "icon.png");
    private Identifier bind11 = new Identifier("terratile", "icon.png");

    private Identifier bind12 = new Identifier("terratile", "icon.png");
    private Identifier bind13 = new Identifier("terratile", "icon.png");

    private Identifier bind14 = new Identifier("terratile", "icon.png");
    private Identifier bind15 = new Identifier("terratile", "icon.png");

//
    private Identifier bind16 = new Identifier("terratile", "icon.png");
    private Identifier bind17 = new Identifier("terratile", "icon.png");

    private Identifier bind18 = new Identifier("terratile", "icon.png");
    private Identifier bind19 = new Identifier("terratile", "icon.png");

    private Identifier bind20 = new Identifier("terratile", "icon.png");
    private Identifier bind21 = new Identifier("terratile", "icon.png");
    @Override
    public void onInitializeClient() {

        keyBindingNorth = createBindingMap(GLFW.GLFW_KEY_U, "moveNorth");
        keyBindingSouth = createBindingMap(GLFW.GLFW_KEY_J, "moveSouth");
        keyBindingEast = createBindingMap(GLFW.GLFW_KEY_H, "moveEast");
        keyBindingWest = createBindingMap(GLFW.GLFW_KEY_K, "moveWest");

        keyBindingUp = createBindingMap(GLFW.GLFW_KEY_Y, "moveUp");
        keyBindingDown = createBindingMap(GLFW.GLFW_KEY_I, "moveDown");

        keyBindingStart = createBindingMap(GLFW.GLFW_KEY_P, "start");

        keyBindingRotateLeft = createBindingMap(GLFW.GLFW_KEY_N, "rotateLeft");
        keyBindingRotateRight = createBindingMap(GLFW.GLFW_KEY_M, "rotateRight");

        keyBindingScaleUp = createBindingMap(GLFW.GLFW_KEY_G, "scaleUp");
        keyBindingScaleDown = createBindingMap(GLFW.GLFW_KEY_B, "scaleDown");

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            dispatcher.register(ClientCommandManager.literal("terratile")
                    .then(ClientCommandManager.literal("select")
                            .then(ClientCommandManager.argument("coordinates", StringArgumentType.greedyString())
                                    .executes(context -> {
                                        String coordinates = StringArgumentType.getString(context, "coordinates");
                                        String[] parts = coordinates.split(",");

                                        if (parts.length == 2) {
                                            try {
                                                double latitude = Double.parseDouble(parts[0]);
                                                double longitude = Double.parseDouble(parts[1]);

                                                selectedLatitude = latitude;
                                                selectedLongitude = longitude;

                                                context.getSource().sendFeedback(Text.literal("Location set. Please reload the map."));

                                                return 1;
                                            } catch (NumberFormatException e) {
                                                context.getSource().sendFeedback(Text.literal("Invalid coordinates format. Use LAT, LNG."));
                                            }
                                        } else {
                                            context.getSource().sendFeedback(Text.literal("Invalid coordinates format. Use LAT, LNG."));

                                        }

                                        return 0;
                                    })
                            )
                    )
            );
        });

        ClientTickEvents.END_CLIENT_TICK.register((MinecraftClient client) -> {
            while (keyBindingNorth.wasPressed()) {
                z -= 0.5;
            }
            while (keyBindingSouth.wasPressed()) {
                z += 0.5;
            }
            while (keyBindingEast.wasPressed()) {
                x -= 0.5;
            }
            while (keyBindingWest.wasPressed()) {
                x += 0.5;
            }
            while (keyBindingUp.wasPressed()) {
                y -= 0.5;
            }
            while (keyBindingDown.wasPressed()) {
                y += 0.5;
            }

            while (keyBindingScaleUp.wasPressed()) {
                scale += 1;
            }

            while (keyBindingScaleDown.wasPressed()) {
                scale -= 1;
            }


            while (keyBindingStart.wasPressed()) {

                if (isDisplayed) {
                    isDisplayed = false;
                } else {
                    ClientPlayerEntity playerEntity = MinecraftClient.getInstance().player;

                    assert playerEntity != null;
                    x = playerEntity.getX();
                    z = playerEntity.getZ();


                    int[] xyzTileCoords = convertLatLngToTile(selectedLatitude, selectedLongitude, 18);

                    NativeImageBackedTexture nativeImageBackedTexture = retrieveTile(xyzTileCoords[0], xyzTileCoords[1]);
                    NativeImageBackedTexture nativeImageBackedTexture1 = retrieveTile(xyzTileCoords[0] + 1, xyzTileCoords[1]);
                    NativeImageBackedTexture nativeImageBackedTexture2 = retrieveTile(xyzTileCoords[0] - 1, xyzTileCoords[1]);

                    NativeImageBackedTexture nativeImageBackedTexture3 = retrieveTile(xyzTileCoords[0], xyzTileCoords[1] + 1);
                    NativeImageBackedTexture nativeImageBackedTexture4 = retrieveTile(xyzTileCoords[0] + 1, xyzTileCoords[1] + 1);
                    NativeImageBackedTexture nativeImageBackedTexture5 = retrieveTile(xyzTileCoords[0] - 1, xyzTileCoords[1] + 1);

                    NativeImageBackedTexture nativeImageBackedTexture6 = retrieveTile(xyzTileCoords[0], xyzTileCoords[1] - 1);
                    NativeImageBackedTexture nativeImageBackedTexture7 = retrieveTile(xyzTileCoords[0] + 1, xyzTileCoords[1] - 1);
                    NativeImageBackedTexture nativeImageBackedTexture8 = retrieveTile(xyzTileCoords[0] - 1, xyzTileCoords[1] - 1);

//
                    NativeImageBackedTexture nativeImageBackedTexture9 = retrieveTile(xyzTileCoords[0] + 2, xyzTileCoords[1]);
                    NativeImageBackedTexture nativeImageBackedTexture10 = retrieveTile(xyzTileCoords[0] - 2, xyzTileCoords[1]);

                    NativeImageBackedTexture nativeImageBackedTexture11 = retrieveTile(xyzTileCoords[0] + 2, xyzTileCoords[1] + 1);
                    NativeImageBackedTexture nativeImageBackedTexture12 = retrieveTile(xyzTileCoords[0] - 2, xyzTileCoords[1] + 1);

                    NativeImageBackedTexture nativeImageBackedTexture13 = retrieveTile(xyzTileCoords[0] + 2, xyzTileCoords[1] - 1);
                    NativeImageBackedTexture nativeImageBackedTexture14 = retrieveTile(xyzTileCoords[0] - 2, xyzTileCoords[1] - 1);
//

                    TextureManager textureManager = MinecraftClient.getInstance().getTextureManager();
                    bind1 = textureManager.registerDynamicTexture("terratile", nativeImageBackedTexture);
                    bind2 = textureManager.registerDynamicTexture("terratile", nativeImageBackedTexture1);
                    bind3 = textureManager.registerDynamicTexture("terratile", nativeImageBackedTexture2);

                    bind4 = textureManager.registerDynamicTexture("terratile", nativeImageBackedTexture3);
                    bind5 = textureManager.registerDynamicTexture("terratile", nativeImageBackedTexture4);
                    bind6 = textureManager.registerDynamicTexture("terratile", nativeImageBackedTexture5);

                    bind7 = textureManager.registerDynamicTexture("terratile", nativeImageBackedTexture6);
                    bind8 = textureManager.registerDynamicTexture("terratile", nativeImageBackedTexture7);
                    bind9 = textureManager.registerDynamicTexture("terratile", nativeImageBackedTexture8);

                    bind10 = textureManager.registerDynamicTexture("terratile", nativeImageBackedTexture9);
                    bind11 = textureManager.registerDynamicTexture("terratile", nativeImageBackedTexture10);

                    bind12 = textureManager.registerDynamicTexture("terratile", nativeImageBackedTexture11);
                    bind13 = textureManager.registerDynamicTexture("terratile", nativeImageBackedTexture12);

                    bind14 = textureManager.registerDynamicTexture("terratile", nativeImageBackedTexture13);
                    bind15 = textureManager.registerDynamicTexture("terratile", nativeImageBackedTexture14);
                    isDisplayed = true;
                }


            }

            while (keyBindingRotateLeft.wasPressed()) {
                if (rotationDegree >= -90) {
                    rotationDegree -= 1;
                }
            }
            while (keyBindingRotateRight.wasPressed()) {
                if (rotationDegree <= 90) {
                    rotationDegree += 1;
                }
            }
        });

        WorldRenderEvents.END.register(context -> {
            if (isDisplayed) {
                Camera camera = context.camera();

                Vec3d targetPosition = new Vec3d(x, y, z);
                Vec3d transformedPosition = targetPosition.subtract(camera.getPos());

                MatrixStack matrixStack = new MatrixStack();

                matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
                matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(camera.getYaw() + 180.0F));

                matrixStack.translate(transformedPosition.x, transformedPosition.y, transformedPosition.z);

                Matrix4f positionMatrix = matrixStack.peek().getPositionMatrix();
                Tessellator tessellator = Tessellator.getInstance();

                double[] mapCenterPoint = {0, 0};

                drawTile(tessellator, positionMatrix, bind1, mapCenterPoint);
                drawTile(tessellator, positionMatrix, bind2, calculateNewPoint(new double[]{scale, 0}, mapCenterPoint, rotationDegree));
                drawTile(tessellator, positionMatrix, bind3, calculateNewPoint(new double[]{-scale, 0}, mapCenterPoint, rotationDegree));

                drawTile(tessellator, positionMatrix, bind4, calculateNewPoint(new double[]{0, scale}, mapCenterPoint, rotationDegree));
                drawTile(tessellator, positionMatrix, bind5, calculateNewPoint(new double[]{scale, scale}, mapCenterPoint, rotationDegree));
                drawTile(tessellator, positionMatrix, bind6, calculateNewPoint(new double[]{-scale, scale}, mapCenterPoint, rotationDegree));

                drawTile(tessellator, positionMatrix, bind7, calculateNewPoint(new double[]{0, -scale}, mapCenterPoint, rotationDegree));
                drawTile(tessellator, positionMatrix, bind8, calculateNewPoint(new double[]{scale, -scale}, mapCenterPoint, rotationDegree));
                drawTile(tessellator, positionMatrix, bind9, calculateNewPoint(new double[]{-scale, -scale}, mapCenterPoint, rotationDegree));

//                -------
                drawTile(tessellator, positionMatrix, bind10, calculateNewPoint(new double[]{scale * 2, 0}, mapCenterPoint, rotationDegree));
                drawTile(tessellator, positionMatrix, bind11, calculateNewPoint(new double[]{-scale * 2, 0}, mapCenterPoint, rotationDegree));

                drawTile(tessellator, positionMatrix, bind12, calculateNewPoint(new double[]{scale * 2, scale}, mapCenterPoint, rotationDegree));
                drawTile(tessellator, positionMatrix, bind13, calculateNewPoint(new double[]{-scale * 2, scale}, mapCenterPoint, rotationDegree));

                drawTile(tessellator, positionMatrix, bind14, calculateNewPoint(new double[]{scale * 2, -scale}, mapCenterPoint, rotationDegree));
                drawTile(tessellator, positionMatrix, bind15, calculateNewPoint(new double[]{-scale * 2, -scale}, mapCenterPoint, rotationDegree));
            }

        });
    }

    public KeyBinding createBindingMap(int glfwKey, String translationKey) {
        return KeyBindingHelper.registerKeyBinding(new KeyBinding("key.terratile" + translationKey,
                InputUtil.Type.KEYSYM,
                glfwKey,
                "category.terratile.mapcontrols"
        ));
    }

    public int[] convertLatLngToTile(double lat, double lng, int zoom) {
        int x = (int) Math.floor((lng + 180) / 360 * Math.pow(2, zoom));
        int y = (int) Math.floor((1 - Math.log(Math.tan(Math.toRadians(lat)) + 1 / Math.cos(Math.toRadians(lat))) / Math.PI) / 2 * Math.pow(2, zoom));

        return new int[]{x, y};
    }

    public NativeImageBackedTexture retrieveTile(int x, int y) {
        try {
            URL textureUrl = new URL("http://localhost:3000/18/" + x + "/" + y);
            System.out.println(textureUrl);
            InputStream inputStream;
            inputStream = textureUrl.openStream();
            return new NativeImageBackedTexture(NativeImage.read(inputStream));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

//  Everything beyond this point is quick maffs, just to rotate the map tile.

    public void drawTile(Tessellator tessellator, Matrix4f positionMatrix, Identifier tile, double[] centerPoint) {
        BufferBuilder buffer1 = tessellator.getBuffer();

        double[] firstPoint = calculateNewPoint(new double[]{centerPoint[0] - scale/2, centerPoint[1] - scale/2}, centerPoint, rotationDegree);
        double[] secondPoint = calculateNewPoint(new double[]{centerPoint[0] - scale/2, centerPoint[1] + scale/2}, centerPoint, rotationDegree);
        double[] thirdPoint = calculateNewPoint(new double[]{centerPoint[0] + scale/2, centerPoint[1] + scale/2}, centerPoint, rotationDegree);
        double[] fourthPoint = calculateNewPoint(new double[]{centerPoint[0] + scale/2, centerPoint[1] - scale/2}, centerPoint, rotationDegree);

        buffer1.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        buffer1.vertex(positionMatrix, (float) firstPoint[0], 0, (float) firstPoint[1]).texture(0f, 0f).next();
        buffer1.vertex(positionMatrix, (float) secondPoint[0], 0, (float) secondPoint[1]).texture(0f, 1f).next();
        buffer1.vertex(positionMatrix, (float) thirdPoint[0], 0, (float) thirdPoint[1]).texture(1f, 1f).next();
        buffer1.vertex(positionMatrix, (float) fourthPoint[0], 0, (float) fourthPoint[1]).texture(1f, 0f).next();

        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderTexture(0, tile);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.disableCull();

        tessellator.draw();

        RenderSystem.depthFunc(GL11.GL_LEQUAL);
        RenderSystem.enableCull();
    }

    public double[] calculateNewPoint(double[] originalPoint, double[] centerPoint, double baseAngle) {

        double[] equation = deduceLinearEquation(originalPoint, centerPoint);

        double[] firstEquation = calculateNewEquation(centerPoint, equation, baseAngle);

        double vertexAngle = findVertexAngle(baseAngle);

        double[] secondEquation = calculateNewEquation(originalPoint, equation, 180 - vertexAngle);

        return findIntersectionPoint(firstEquation, secondEquation);

    }

    public double[] deduceLinearEquation(double[] point1, double[] point2) {
        // Calculate the slope (m)
        double slope = (point2[1] - point1[1]) / (point2[0] - point1[0]);

        // Calculate the y-intercept (b)
        double intercept = point1[1] - slope * point1[0];

        return new double[]{slope, intercept};
    }

    public double[] calculateNewEquation(double[] point, double[] originalEquation, double angleDegrees) {
        // Convert angle from degrees to radians
        double angleRadians = Math.toRadians(angleDegrees);

        // Calculate the slope of the new equation
        double newSlope = Math.tan(angleRadians + Math.atan(originalEquation[0]));

        // Means linear equation is vertical
        if (newSlope > 1000) {
            newSlope = 100000000;
        }

        // Calculate the y-intercept of the new equation
        double newIntercept = point[1] - newSlope * point[0];

        return new double[]{newSlope, newIntercept};
    }

    public double findVertexAngle(double angleDegrees) {
        return (180 - angleDegrees) / 2;
    }

    public static double[] findIntersectionPoint(double[] equation1, double[] equation2) {
        double x = (equation2[1] - equation1[1]) / (equation1[0] - equation2[0]);
        double y = equation1[0] * x + equation1[1];

        return new double[]{x, y};
    }

}