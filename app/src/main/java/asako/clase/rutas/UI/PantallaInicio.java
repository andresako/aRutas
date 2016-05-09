package asako.clase.rutas.UI;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import asako.clase.rutas.Tools.MiConfig;
import asako.clase.rutas.R;

public class PantallaInicio extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private FragmentManager fragmentManager;
    public MiConfig datos;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        datos = MiConfig.get();
        super.onCreate(savedInstanceState);

        sp = PreferenceManager.getDefaultSharedPreferences(this);
        setContentView(R.layout.activity_pantalla_inicio);

        fragmentManager = getSupportFragmentManager();
        agregarToolbar();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        drawerLayout.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);

        if (navigationView != null) {
            prepararDrawer(navigationView);
            seleccionarItem(navigationView.getMenu().getItem(0));
        }

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    private void agregarToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setHomeAsUpIndicator(R.drawable.drawer_toggle);
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void prepararDrawer(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                seleccionarItem(menuItem);
                drawerLayout.closeDrawers();
                return true;
            }
        });
        LinearLayout llOff = (LinearLayout) findViewById(R.id.llOff);
        if (llOff != null) {
            llOff.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences.Editor edit = sp.edit();
                    edit.putBoolean("recordarContra", false);
                    edit.putString("password", "");
                    edit.apply();
                    finish();
                    startActivity(new Intent(PantallaInicio.this, Login.class));
                }
            });
        }
    }

    private void seleccionarItem(MenuItem itemDrawer) {
        Fragment fragmentoGenerico = null;

        switch (itemDrawer.getItemId()) {
            case R.id.item_inicio:
                fragmentoGenerico = new FragmentoInicio();
                break;

            case R.id.item_pInteres:
                fragmentoGenerico = new FragmentoPuntos();
                break;

            case R.id.item_rutas:
                fragmentoGenerico = new FragmentoRutas();
                break;

            case R.id.item_cuenta:
                fragmentoGenerico = new FragmentoCuenta();
                break;

            case R.id.item_mapa:
                startActivity(new Intent(this, MapsActivity.class));
                break;

            case R.id.item_configuracion:
                //startActivity(new Intent(this, MapsActivity.class));
                break;
        }
        if (fragmentoGenerico != null) {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.contenedor_principal, fragmentoGenerico)
                    .commit();
            // Setear título actual
            setTitle(itemDrawer.getTitle());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_actividad_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return false;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (fragmentManager.getBackStackEntryCount() != 0) {
            if (fragmentManager.findFragmentByTag("puntoActivo") != null) {
                if (((FragmentoPunto) fragmentManager.findFragmentByTag("puntoActivo")).editado) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(PantallaInicio.this);
                    alert.setTitle("Quiere guardar los cambios?");
                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    //((FragmentoPunto) fragmentManager.findFragmentByTag("puntoActivo")).guardarPunto();
                                    fragmentManager.popBackStackImmediate();
                                }
                            }
                    );
                    alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    fragmentManager.popBackStackImmediate();
                                }
                            }
                    );
                    alert.show();
                } else {
                    fragmentManager.popBackStackImmediate();
                }

            } else if (fragmentManager.findFragmentByTag("salidaActiva") != null) {
                AlertDialog.Builder alert = new AlertDialog.Builder(PantallaInicio.this);
                alert.setTitle("Quiere guardar los cambios?");
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                //((FragmentoPunto) fragmentManager.findFragmentByTag("puntoActivo")).guardarPunto();
                                fragmentManager.popBackStackImmediate();
                            }
                        }
                );
                alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                fragmentManager.popBackStackImmediate();
                            }
                        }
                );
                alert.show();

            }
        } else {
            AlertDialog.Builder alert = new AlertDialog.Builder(PantallaInicio.this);
            alert.setTitle("Quiere salir de la apicación?");
            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            finish();
                        }
                    }
            );
            alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            //Do nothing
                        }
                    }
            );
            alert.show();
        }
    }
}

