#include <AR/gsub_es.h>
#include <Eden/glm.h>
#include <jni.h>
#include <ARWrapper/ARToolKitWrapperExportedAPI.h>
#include <unistd.h> // chdir()
#include <android/log.h>

// Utility preprocessor directive so only one change needed if Java class name changes
#define JNIFUNCTION_DEMO(sig) Java_org_artoolkit_ar_samples_ARSimpleNativeCars_SimpleNativeRenderer_##sig

extern "C" {
JNIEXPORT void JNICALL
JNIFUNCTION_DEMO(demoInitialise(JNIEnv * env, jobject
                         object));
JNIEXPORT void JNICALL
JNIFUNCTION_DEMO(demoShutdown(JNIEnv * env, jobject
                         object));
JNIEXPORT void JNICALL
JNIFUNCTION_DEMO(demoSurfaceCreated(JNIEnv * env, jobject
                         object));
JNIEXPORT void JNICALL
JNIFUNCTION_DEMO(demoSurfaceChanged(JNIEnv * env, jobject
                         object, jint
                         w, jint
                         h));
JNIEXPORT void JNICALL
JNIFUNCTION_DEMO(demoDrawFrame(JNIEnv * env, jobject
                         obj));

JNIEXPORT jintArray JNICALL
JNIFUNCTION_DEMO(getArrayMarkersID(JNIEnv * env, jobject
                         obj));

JNIEXPORT void JNICALL
JNIFUNCTION_DEMO(selectMarkerByID(JNIEnv * env, jobject
                         obj, jint
                         id));

JNIEXPORT void JNICALL
JNIFUNCTION_DEMO(changeOffsetTranslation(JNIEnv * env, jobject
                         obj, jint
                         id, jfloat
                         x, jfloat
                         y, jfloat
                         z));

JNIEXPORT void JNICALL
JNIFUNCTION_DEMO(changeOffsetRotationX(JNIEnv * env, jobject
                         obj, jint
                         id, jfloat
                         angle, jfloat
                         value));

JNIEXPORT void JNICALL
JNIFUNCTION_DEMO(changeOffsetRotationY(JNIEnv * env, jobject
                         obj, jint
                         id, jfloat
                         angle, jfloat
                         value));

JNIEXPORT void JNICALL
JNIFUNCTION_DEMO(changeOffsetRotationZ(JNIEnv * env, jobject
                         obj, jint
                         id, jfloat
                         angle, jfloat
                         value));

JNIEXPORT void JNICALL
JNIFUNCTION_DEMO(changeOffsetScale(JNIEnv * env, jobject
                         obj, jint
                         id, jfloat
                         x, jfloat
                         y, jfloat
                         z));
};

typedef struct ARModel {
    int patternID;
    ARdouble transformationMatrix[16];
    bool visible;
    GLMmodel *obj;
    bool selected;
    GLfloat offset_translation[3];
    GLfloat offset_rotation_X[2];
    GLfloat offset_rotation_Y[2];
    GLfloat offset_rotation_Z[2];
    GLfloat offset_scale[3];
} ARModel;

#define NUM_MODELS 4
static ARModel models[NUM_MODELS] = {0};

static float lightAmbient[4] = {0.1f, 0.1f, 0.1f, 1.0f};
static float lightDiffuse[4] = {1.0f, 1.0f, 1.0f, 1.0f};
static float lightPosition[4] = {0.0f, 0.0f, 1.0f, 0.0f};

JNIEXPORT void JNICALL
JNIFUNCTION_DEMO(demoInitialise(JNIEnv * env, jobject
                         object)) {

    const char *model0file = "Data/models/Porsche_911_GT3.obj";
    const char *model1file = "Data/models/Ferrari_Modena_Spider.obj";
    const char *model2file = "Data/models/marco_polo_ideale_770.obj";
    const char *model3file = "Data/models/Chevrolet_Camaro_Highway_Patrol.obj";

    models[0].patternID = arwAddMarker("single;Data/multi/patt.a;80");
    arwSetMarkerOptionBool(models[0].patternID, ARW_MARKER_OPTION_SQUARE_USE_CONT_POSE_ESTIMATION,
                           false);
    arwSetMarkerOptionBool(models[0].patternID, ARW_MARKER_OPTION_FILTERED, true);

    models[0].obj = glmReadOBJ2(model0file, 0, 0); // context 0, don't read textures yet.
    if (!models[0].obj) {
        LOGE("Error loading model from file '%s'.", model0file);
        exit(-1);
    }
    glmScale(models[0].obj, 0.05f);
    glmRotate(models[0].obj, 3.14159f / 2.0f, 0.0f, 0.0f, 1.0f);
    glmRotate(models[0].obj, 3.14159f / 2.0f, -1.0f, 0.0f, 0.0f);
    glmRotate(models[0].obj, 3.14159f / 2.0f, 0.0f, 1.0f, 0.0f);
    GLfloat translate0[3] = {0.0f, -25.0f, 0.0f};
    glmTranslate(models[0].obj, translate0);
    glmCreateArrays(models[0].obj, GLM_SMOOTH | GLM_MATERIAL | GLM_TEXTURE);
    models[0].visible = false;

    models[1].patternID = arwAddMarker("single;Data/multi/patt.b;80");
    arwSetMarkerOptionBool(models[1].patternID, ARW_MARKER_OPTION_SQUARE_USE_CONT_POSE_ESTIMATION,
                           false);
    arwSetMarkerOptionBool(models[1].patternID, ARW_MARKER_OPTION_FILTERED, true);

    models[1].obj = glmReadOBJ2(model1file, 0, 0); // context 0, don't read textures yet.
    if (!models[1].obj) {
        LOGE("Error loading model from file '%s'.", model1file);
        exit(-1);
    }
    glmScale(models[1].obj, 0.05f);
    glmRotate(models[1].obj, 3.14159f / 2.0f, 0.0f, 0.0f, 1.0f);
    glmRotate(models[1].obj, 3.14159f / 2.0f, -1.0f, 0.0f, 0.0f);
    GLfloat translate1[3] = {0.0f, -25.0f, 0.0f};
    glmTranslate(models[1].obj, translate1);
    glmCreateArrays(models[1].obj, GLM_SMOOTH | GLM_MATERIAL | GLM_TEXTURE);
    models[1].visible = false;

    models[2].patternID = arwAddMarker("single;Data/multi/patt.c;80");
    arwSetMarkerOptionBool(models[2].patternID, ARW_MARKER_OPTION_SQUARE_USE_CONT_POSE_ESTIMATION,
                           false);
    arwSetMarkerOptionBool(models[2].patternID, ARW_MARKER_OPTION_FILTERED, true);

    models[2].obj = glmReadOBJ2(model2file, 0, 0); // context 0, don't read textures yet.
    if (!models[2].obj) {
        LOGE("Error loading model from file '%s'.", model0file);
        exit(-1);
    }
    glmScale(models[2].obj, 35.0f);
    glmRotate(models[2].obj, 3.14159f / 2.0f, 0.0f, -1.0f, 0.0f);
    GLfloat translate2[3] = {20.0f, 0.0f, 0.0f};
    glmTranslate(models[2].obj, translate2);
    glmCreateArrays(models[2].obj, GLM_SMOOTH | GLM_MATERIAL | GLM_TEXTURE);
    models[2].visible = false;

    models[3].patternID = arwAddMarker("single;Data/multi/patt.d;80");
    arwSetMarkerOptionBool(models[3].patternID, ARW_MARKER_OPTION_SQUARE_USE_CONT_POSE_ESTIMATION,
                           false);
    arwSetMarkerOptionBool(models[3].patternID, ARW_MARKER_OPTION_FILTERED, true);

    models[3].obj = glmReadOBJ2(model3file, 0, 0); // context 0, don't read textures yet.
    if (!models[3].obj) {
        LOGE("Error loading model from file '%s'.", model0file);
        exit(-1);
    }
    glmScale(models[3].obj, 15.0f);
    glmRotate(models[3].obj, 3.14159f / 2.0f, 0.0f, 1.0f, 0.0f);
    glmCreateArrays(models[3].obj, GLM_SMOOTH | GLM_MATERIAL | GLM_TEXTURE);
    models[3].visible = false;
}

JNIEXPORT void JNICALL
JNIFUNCTION_DEMO(demoShutdown(JNIEnv * env, jobject
                         object)) {
}

JNIEXPORT void JNICALL
JNIFUNCTION_DEMO(demoSurfaceCreated(JNIEnv * env, jobject
                         object)) {
    glStateCacheFlush(); // Make sure we don't hold outdated OpenGL state.
    for (int i = 0; i < NUM_MODELS; i++) {
        if (models[i].obj) {
            glmDelete(models[i].obj, 0);
            models[i].obj = NULL;
        }
    }
}

JNIEXPORT void JNICALL
JNIFUNCTION_DEMO(demoSurfaceChanged(JNIEnv * env, jobject
                         object, jint
                         w, jint
                         h)) {
    // glViewport(0, 0, w, h) has already been set.
}

JNIEXPORT void JNICALL
JNIFUNCTION_DEMO(demoDrawFrame(JNIEnv * env, jobject
                         obj)) {
    glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

    // Set the projection matrix to that provided by ARToolKit.
    float proj[16];
    arwGetProjectionMatrix(proj);
    glMatrixMode(GL_PROJECTION);
    glLoadMatrixf(proj);
    glMatrixMode(GL_MODELVIEW);

    glStateCacheEnableDepthTest();

    glStateCacheEnableLighting();

    glEnable(GL_LIGHT0);

    //LOGE("Models selected '%d' '%d' '%d' '%d'",
    //     models[0].selected, models[1].selected, models[2].selected, models[3].selected);

    for (int i = 0; i < NUM_MODELS; i++) {
        models[i].visible = arwQueryMarkerTransformation(models[i].patternID,
                                                         models[i].transformationMatrix);
        if (models[i].visible) {
            glLoadMatrixf(models[i].transformationMatrix);
            glPushMatrix();

            float lightDiffuse[4] = {1.0f, 1.0f, 1.0f, 1.0f};
            if (models[i].selected) {
                lightDiffuse[0] = 0.0f;
                lightDiffuse[1] = 1.0f;
                lightDiffuse[2] = 0.0f;
                lightDiffuse[3] = 0.0f;
            }

            glLightfv(GL_LIGHT0, GL_AMBIENT, lightAmbient);
            glLightfv(GL_LIGHT0, GL_DIFFUSE, lightDiffuse);
            glLightfv(GL_LIGHT0, GL_POSITION, lightPosition);

            //Changing translation of the model
            if (models[i].offset_translation[0] != 0.0) {
                glTranslatef(models[i].offset_translation[0], 0.0f, 0.0f);
            }
            if (models[i].offset_translation[1] != 0.0) {
                glTranslatef(0.0f, models[i].offset_translation[1], 0.0f);
            }
            if (models[i].offset_translation[2] != 0.0) {
                glTranslatef(0.0f, 0.0f, models[i].offset_translation[2]);
            }
            glmDrawArrays(models[i].obj, 0);
            glPopMatrix();
        }
    }
}

JNIEXPORT jintArray JNICALL
JNIFUNCTION_DEMO(getArrayMarkersID(JNIEnv * env, jobject
                         obj)) {
    jintArray newArray = env->NewIntArray(NUM_MODELS);
    jint *idArray = env->GetIntArrayElements(newArray, NULL);

    for (int i = 0; i < NUM_MODELS; i++) {
        idArray[i] = models[i].patternID;
    }

    env->ReleaseIntArrayElements(newArray, idArray, NULL);

    return newArray;
}

JNIEXPORT void JNICALL
JNIFUNCTION_DEMO(selectMarkerByID(JNIEnv * env, jobject
                         obj, jint
                         id)) {
    for (int i = 0; i < NUM_MODELS; i++) {
        models[i].selected = (models[i].patternID == id);
    }
}

JNIEXPORT void JNICALL
JNIFUNCTION_DEMO(changeOffsetTranslation(JNIEnv * env, jobject
                         obj, jint
                         id, jfloat
                         x, jfloat
                         y, jfloat
                         z)) {
    models[id].offset_translation[0] += x;
    models[id].offset_translation[1] += y;
    models[id].offset_translation[2] += z;
}

JNIEXPORT void JNICALL
JNIFUNCTION_DEMO(changeOffsetRotationX(JNIEnv * env, jobject
                         obj, jint
                         id, jfloat
                         angle, jfloat
                         value)) {
    models[id].offset_rotation_X[0] = angle;
    models[id].offset_rotation_X[1] = value;
}

JNIEXPORT void JNICALL
JNIFUNCTION_DEMO(changeOffsetRotationY(JNIEnv * env, jobject
                         obj, jint
                         id, jfloat
                         angle, jfloat
                         value)) {
    models[id].offset_rotation_Y[0] = angle;
    models[id].offset_rotation_Y[1] = value;
}

JNIEXPORT void JNICALL
JNIFUNCTION_DEMO(changeOffsetRotationZ(JNIEnv * env, jobject
                         obj, jint
                         id, jfloat
                         angle, jfloat
                         value)) {
    models[id].offset_rotation_Z[0] = angle;
    models[id].offset_rotation_Z[1] = value;
}

JNIEXPORT void JNICALL
JNIFUNCTION_DEMO(changeOffsetScale(JNIEnv * env, jobject
                         obj, jint
                         id, jfloat
                         x, jfloat
                         y, jfloat
                         z)) {
    models[id].offset_scale[0] += x;
    models[id].offset_scale[1] += y;
    models[id].offset_scale[2] += z;
}