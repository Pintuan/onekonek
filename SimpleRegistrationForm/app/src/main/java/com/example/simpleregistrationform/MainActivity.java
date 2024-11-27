<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
            xmlns:app="http://schemas.android.com/apk/res-auto"
            xmlns:tools="http://schemas.android.com/tools"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:orientation="vertical"
            android:padding="16dp"
            tools:context=".MainActivity">

<androidx.recyclerview.widget.RecyclerView
        android:id="@+id/recyclerView"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:scrollbars="vertical" />

</LinearLayout>
        holder.addToCartButton.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View view) {
        String itemName = items.get(position).getName();
        String itemSize = items.get(position).getSize();
        Toast.makeText(context, "Added to Cart: " + itemName + " (Size " + itemSize + ")", Toast.LENGTH_SHORT).show();
        }
        });
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        ItemAdapter adapter = new ItemAdapter(itemList); // Create an adapter with your item list
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
