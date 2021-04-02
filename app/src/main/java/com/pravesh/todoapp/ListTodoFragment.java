package com.pravesh.todoapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pravesh.todoapp.model.ETodo;
import com.pravesh.todoapp.viewModel.TodoViewModel;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListTodoFragment extends Fragment {
    View rootView;
    RecyclerView rvListTodo;
    TodoViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_list_todo, container, false);
        rvListTodo = rootView.findViewById(R.id.list_item_todo_rv_list_todo);
        viewModel = new ViewModelProvider(this).get(TodoViewModel.class);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rvListTodo.setLayoutManager(manager);
        updateRV();

        new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView,
                                          @NonNull RecyclerView.ViewHolder viewHolder,
                                          @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                        TodoAdaptor adaptor = new TodoAdaptor(getTodos(viewModel.getAllTodos().getValue()));
                        ETodo todo = adaptor.getTodoAt(viewHolder.getAdapterPosition());
                        if (todo != null) {
                            viewModel.deleteById(todo);
                            Toast.makeText(getContext(), "Todo successfully deleted!", Toast.LENGTH_SHORT).show();
                        }

                    }
                }).attachToRecyclerView(rvListTodo);

        return rootView;
    }

    // Convert list of todos into hashMap with keys
    private HashMap<String, List<ETodo>> groupIntoHashMap(List<ETodo> eTodos) {
        HashMap<String, List<ETodo>> hashMap = new HashMap<>();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (ETodo eTodo : eTodos) {
            LocalDate date = eTodo.getTodoDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            if (date.isEqual(LocalDate.now())) {
                String key = "Today";

                if (hashMap.containsKey("Today")) {
                    hashMap.get("Today").add(eTodo);
                } else {
                    List<ETodo> list = new ArrayList<>();
                    list.add(eTodo);
                    hashMap.put(key, list);
                }
            } else if (date.isEqual(LocalDate.now().plusDays(1))) {
                String key = "Tomorrow";

                if (hashMap.containsKey("Tomorrow")) {
                    hashMap.get("Tomorrow").add(eTodo);
                } else {
                    List<ETodo> list = new ArrayList<>();
                    list.add(eTodo);
                    hashMap.put(key, list);
                }
            } else if (date.isBefore(LocalDate.now())) {
                String key = "Older";

                if (hashMap.containsKey("Older")) {
                    hashMap.get("Older").add(eTodo);
                } else {
                    List<ETodo> list = new ArrayList<>();
                    list.add(eTodo);
                    hashMap.put(key, list);
                }
            } else {
                String key = "Later";

                if (hashMap.containsKey("Later")) {
                    hashMap.get("Later").add(eTodo);
                } else {
                    List<ETodo> list = new ArrayList<>();
                    list.add(eTodo);
                    hashMap.put(key, list);
                }
            }

        }
        return hashMap;
    }

    // Create a sorted list of ListItem form the HashMap
    private List<ListItem> getTodos(List<ETodo> eTodos) {
        HashMap<String, List<ETodo>> hashMap = groupIntoHashMap(eTodos);
        // We linearly add every item into the consolidatedList.
        List<ListItem> consolidatedList = new ArrayList<>();

        if (hashMap.containsKey("Today")) {
            GroupItem dateItem = new GroupItem();
            dateItem.setGroup("Today");
            consolidatedList.add(dateItem);
            for (ETodo todo : hashMap.get("Today")) {
                TodoItem generalItem = new TodoItem();
                generalItem.seteTodo(todo);
                consolidatedList.add(generalItem);
            }
        }

        if (hashMap.containsKey("Tomorrow")) {
            GroupItem dateItem = new GroupItem();
            dateItem.setGroup("Tomorrow");
            consolidatedList.add(dateItem);
            for (ETodo todo : hashMap.get("Tomorrow")) {
                TodoItem generalItem = new TodoItem();
                generalItem.seteTodo(todo);
                consolidatedList.add(generalItem);
            }
        }

        if (hashMap.containsKey("Later")) {
            GroupItem dateItem = new GroupItem();
            dateItem.setGroup("Later");
            consolidatedList.add(dateItem);
            for (ETodo todo : hashMap.get("Later")) {
                TodoItem generalItem = new TodoItem();
                generalItem.seteTodo(todo);
                consolidatedList.add(generalItem);
            }
        }

        if (hashMap.containsKey("Older")) {
            GroupItem dateItem = new GroupItem();
            dateItem.setGroup("Older");
            consolidatedList.add(dateItem);
            for (ETodo todo : hashMap.get("Older")) {
                TodoItem generalItem = new TodoItem();
                generalItem.seteTodo(todo);
                consolidatedList.add(generalItem);
            }
        }

        return consolidatedList;
    }


    void updateRV() {
        viewModel.getAllTodos().observe(getViewLifecycleOwner(), new Observer<List<ETodo>>() {
            @Override
            public void onChanged(List<ETodo> eTodos) {
                TodoAdaptor adaptor = new TodoAdaptor(getTodos(eTodos));
                rvListTodo.setAdapter(adaptor);
            }
        });
    }

    private class TodoHolder extends RecyclerView.ViewHolder {
        TextView titleToday, dateToday, todoDescription;
        ImageView priorityToday;

        public TodoHolder(View view) {
            super(view);

            titleToday = view.findViewById(R.id.list_item_tv_title_today);
            dateToday = view.findViewById(R.id.list_item_tv_date_today);
            priorityToday = view.findViewById(R.id.list_item_tv_priority_today);
            todoDescription = view.findViewById(R.id.todo_description);

            titleToday.setOnClickListener(v -> loadUpdateItem());
            dateToday.setOnClickListener(v -> loadUpdateItem());
            priorityToday.setOnClickListener(v -> loadUpdateItem());
        }

        void loadUpdateItem() {
            TodoAdaptor adaptor = new TodoAdaptor(getTodos(viewModel.getAllTodos().getValue()));
            int i = getAdapterPosition();
            ETodo todo = adaptor.getTodoAt(i);
            if (todo != null) {
                Intent intent = new Intent(getActivity(), EditActivity.class);
                intent.putExtra("TodoId", todo.getId());
                startActivity(intent);
            }
        }

        public void bind(ETodo todo) {

            switch (todo.getPriority()) {
                case 1:
                    priorityToday.setColorFilter(getResources().getColor(R.color.color_high));
                    break;
                case 2:
                    priorityToday.setColorFilter(getResources().getColor(R.color.color_medium));
                    break;
                case 3:
                    priorityToday.setColorFilter(getResources().getColor(R.color.color_low));
                    break;
            }

            if (todo.isCompleted()) {
                titleToday.setPaintFlags(titleToday.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                dateToday.setPaintFlags(dateToday.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                todoDescription.setPaintFlags(todoDescription.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                titleToday.setTextColor(Color.argb(255, 209, 209, 234));
                dateToday.setTextColor(Color.argb(255, 209, 209, 234));
                todoDescription.setTextColor(Color.argb(255, 209, 209, 234));
                priorityToday.setColorFilter(Color.rgb(209, 209, 234));
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            titleToday.setText(todo.getTitle());
            dateToday.setText(sdf.format(todo.getTodoDate()));
            todoDescription.setText(todo.getDescription());
        }
    }

    private class GroupHolder extends RecyclerView.ViewHolder {
        TextView dateTextView;

        public GroupHolder(View view) {
            super(view);
            dateTextView = view.findViewById(R.id.dateTextView);
        }

        public void bind(String title) {

            switch (title) {
                case "Today":
                    dateTextView.setTextColor(Color.rgb(10, 132, 255));
                    break;
                case "Tomorrow":
                    dateTextView.setTextColor(Color.rgb(48, 209, 88));
                    break;
                case "Later":
                    dateTextView.setTextColor(Color.rgb(191, 90, 242));
                    break;
                case "Older":
                    dateTextView.setTextColor(Color.rgb(255, 159, 10));
                    break;
            }

            dateTextView.setText(title);
        }
    }


    private class TodoAdaptor extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        List<ListItem> eTodoList;


        public TodoAdaptor(List<ListItem> eTodoList) {
            this.eTodoList = eTodoList;
        }

        @Override
        public int getItemViewType(int position) {
            return eTodoList.get(position).getType();
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            RecyclerView.ViewHolder viewHolder = null;
            switch (viewType) {

                case ListItem.TYPE_TODO:
                    View v1 = layoutInflater.inflate(R.layout.list_item_todo, parent,
                            false);
                    viewHolder = new TodoHolder(v1);
                    break;

                case ListItem.TYPE_GROUP:
                    View v2 = layoutInflater.inflate(R.layout.list_date_todo, parent, false);
                    viewHolder = new GroupHolder(v2);
                    break;
            }
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            switch (holder.getItemViewType()) {

                case ListItem.TYPE_TODO:
                    TodoItem generalItem = (TodoItem) eTodoList.get(position);
                    TodoHolder generalViewHolder = (TodoHolder) holder;
                    generalViewHolder.bind(generalItem.getEtodo());
                    break;

                case ListItem.TYPE_GROUP:
                    GroupItem dateItem = (GroupItem) eTodoList.get(position);
                    GroupHolder dateViewHolder = (GroupHolder) holder;
                    dateViewHolder.bind(dateItem.getGroup());
                    // Populate date item data here
                    break;
            }
        }

        @Override
        public int getItemCount() {
            return eTodoList != null ? eTodoList.size() : 0;
        }

        public ETodo getTodoAt(int position) {
            if (eTodoList.get(position).getType() == ListItem.TYPE_TODO) {
                return ((TodoItem) eTodoList.get(position)).getEtodo();
            }
            return null;
        }
    }
}