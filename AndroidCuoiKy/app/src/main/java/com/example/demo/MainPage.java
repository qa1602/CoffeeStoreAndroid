package com.example.demo;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.demo.adapter.ItemNewsAdapter;
import com.example.demo.model.Item;
import com.example.demo.model.ItemNews;
import com.example.demo.model.User;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    ItemNewsAdapter itemNewsAdapter;
    RecyclerView itemNews;
    ImageView imageView;
    View header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Fragment()).commit();
            navigationView.setCheckedItem(R.id.nav_user_name);
        }

    LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(MainPage.this, LinearLayoutManager.VERTICAL, false);

        itemNews = findViewById(R.id.itemNews);
        itemNewsAdapter = new ItemNewsAdapter(this);
        itemNewsAdapter.setData(getDataItemNews());
        itemNews.setAdapter(itemNewsAdapter);
        itemNews.setLayoutManager(horizontalLayoutManager);

        imageView = findViewById(R.id.imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMenu = new Intent(MainPage.this, CoffeeMenu.class);
                startActivity(intentMenu);
            }
        });

        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        assert actionbar != null;
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.menu);



    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_user_name:
                Toast.makeText(this, "Tên khách hàng", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_money:
                Toast.makeText(this, "1000000đ" , Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_change:
                AlertDialog.Builder b = new AlertDialog.Builder(MainPage.this);
                b.setTitle("Về chúng tôi");
                b.setMessage("Đây là bài lập trình cuối kỳ môn Android của Quốc Anh và Gia Bảo.");
                b.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        return;
                    }
                });
                AlertDialog alertDialog = b.create();
                alertDialog.show();
                break;
            case R.id.nav_logOut:
                Toast.makeText(this, "Đăng xuất", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainPage.this,MainActivity.class));
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private List<ItemNews> getDataItemNews() {
        List<ItemNews> itemList = new ArrayList<>();

        itemList.add(new ItemNews(
                "Cà phê moka là gì và hương vị của cà phê moka thế nào?",
                R.drawable.tintuc_01,
                "Nếu bạn đam mê là phê mà chưa biết cà phê moka là gì thì quả là một thiếu xót lớn đó. " +
                        "Moka là loại cà phê thuộc chi Arabica, bắt nguồn từ tên thành phố Mocha của Yemen, một nước miền Trung đông...."
        ));
        itemList.add(new ItemNews(
                "Nhiều lợi ích khi thu hoạch cà phê chín",
                R.drawable.tintuc_02,
                "Hiện nay, người dân đang bước vào mùa vụ thu hoạch cà phê. Điều đáng mừng là ngày càng " +
                        "nhiều người dân, hợp tác xã và doanh nghiệp chú trọng thu hái cà phê chín để nâng cao hiệu quả kinh tế, giá trị sản phẩm...."
        ));
        itemList.add(new ItemNews(
                "Xuất khẩu cà phê Việt Nam tiến gần đến mốc 4 tỷ USD",
                R.drawable.tintuc_03,
                "Theo tổng cục Hải Quan đến hết tháng 11, xuất khẩu cà phê của Việt Nam đã mang về hơn " +
                        "3,63 tỷ USD, vượt xa con số 3,07 tỷ USD của năm 2021 và tiến gần kỷ lục 4 tỷ USD mà ngành cà phê hướng đến trong năm nay...."
        ));
        itemList.add(new ItemNews(
                "Cà phê trứng Việt Nam lọt top đặc sản thế giới",
                R.drawable.tintuc_04,
                "Việt Nam là một trong những quốc gia nổi tiếng với cafe thơm ngon đậm đà. Không chỉ đặc sắc" +
                        " bởi những ly cafe đen hay sữa đá truyền thống, cafe Việt Nam còn sáng tạo thêm các hương vị mới thành thức uống" +
                        " ngon lạ. Một trong những công thức cà phê đặc biệt và được biết đến rộng rãi nhất tại Việt Nam đó chính là cà phê trứng của Hà Nội...."
        ));
        itemList.add(new ItemNews(
                "Lợi ích của Cà Phê đối với sức khỏe con người",
                R.drawable.tintuc_05,
                "Cà phê mỗi buổi sáng – biểu tượng của người hướng đến thành công với những lợi ích mang lại cho cơ thể" +
                        " không phải ai cũng biết. Nó được yêu thích bởi cả mùi hương nồng đậm và công dụng kích thích sự tỉnh táo, " +
                        "tập trung làm việc. Nhưng không phải ai cũng biết, lợi ích cà phê mang lại cho sức khỏe của bạn không dừng lại ở sự tỉnh táo."
        ));

        return itemList;
    }

}