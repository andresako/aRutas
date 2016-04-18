package asako.clase.rutas.UI;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import asako.clase.rutas.R;

public class FragmentoCuenta extends Fragment {

    private AppBarLayout appBar;
    private TabLayout tabs;
    private ViewPager viewPager;
    private View padre;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmento_cuenta, container, false);

        if (savedInstanceState == null) {
            padre = (View) container.getParent();
            viewPager = (ViewPager) view.findViewById(R.id.pager);
        }
        return view;
    }

    private void insertarTabs() {
        appBar = (AppBarLayout) padre.findViewById(R.id.appbar);
        tabs = (TabLayout) padre.findViewById(R.id.tab_layout);
        tabs.setTabTextColors(Color.parseColor("#FFFFFF"), Color.parseColor("#FFFFFF"));
        tabs.setVisibility(View.VISIBLE);
    }
    private void poblarViewPager() {
        AdaptadorSecciones adapter = new AdaptadorSecciones(getFragmentManager());
        adapter.addFragment(new FragmentoPerfil(), getString(R.string.titulo_tab_perfil));
        adapter.addFragment(new FragmentoHistorial(), getString(R.string.titulo_tab_historial));
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        insertarTabs();
        poblarViewPager();
        tabs.setupWithViewPager(viewPager);
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        tabs.setVisibility(View.GONE);
        super.onDestroyView();
    }

    public class AdaptadorSecciones extends FragmentStatePagerAdapter {
        private final List<Fragment> fragmentos = new ArrayList<>();
        private final List<String> titulosFragmentos = new ArrayList<>();

        public AdaptadorSecciones(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentos.get(position);
        }

        @Override
        public int getCount() {
            return fragmentos.size();
        }

        public void addFragment(Fragment fragment, String title) {
            fragmentos.add(fragment);
            titulosFragmentos.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titulosFragmentos.get(position);
        }
    }
}
