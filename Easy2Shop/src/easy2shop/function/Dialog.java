package easy2shop.function;

import java.util.Calendar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;

public class Dialog
{
/*    public static void ShowMessageDialog(Context context, String message)
    {
        ShowDialog(context,message,MessageBoxType.OKOnly,new String[]{},false, null,null);
    }

    public static void ShowMessageDialog(Context context, String message, MessageBoxType type , OnClickListener listener)
     {
         ShowDialog(context,message,type,new String[]{},false, listener,null);
     }

    public static void ShowListDialog(Context context, String message, String[] listItems, boolean isMultiChoice, OnClickListener listener)
     {
     if (isMultiChoice)
         ShowDialog(context, message, MessageBoxType.OkCancel , listItems, isMultiChoice, listener,null);
     else
         ShowDialog(context, message, MessageBoxType.OKOnly , listItems, isMultiChoice, null,listener);
     }

    public static void ShowDateDialog(Context context,String message,OnDateSetListener listener)
    {
        Calendar c=Calendar.getInstance();
        int y=c.get(Calendar.YEAR);
        int m=c.get(Calendar.MONTH);
        int d=c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dlg=new DatePickerDialog(context, listener, y, m, d);
        dlg.setTitle(message);
        dlg.show();
    }
    
    private static void ShowDialog(Context context, String message, MessageBoxType type , String[] listItems, boolean isMultiChoice, OnClickListener listener,OnClickListener selectedItemListener)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(context);

        if (listItems.length>0 && isMultiChoice==false)
        {
            CheckedItems=new ArrayList();//won't be used in this case.
            builder.setTitle(message);

            builder.setItems(listItems, selectedItemListener);
        }
        else if (listItems.length>0 && isMultiChoice==true)
        {
            CheckedItems=new ArrayList();
            builder.setTitle(message);

            builder.setMultiChoiceItems(listItems, null, new OnMultiChoiceClickListener() 
            {
                @Override
                public void onClick(DialogInterface dialog, int which, boolean checked) 
                {
                    if (checked)
                        CheckedItems.add(which);
                    else
                    {
                        if (CheckedItems.contains(which))
                            CheckedItems.remove(which);
                    }
                }
            });
        }
        else
        {
            builder.setTitle("Milk supply tracker");
            builder.setMessage(message);
        }

        if (listItems.length==0 || isMultiChoice)
        {
            switch(type)
            {
            case OKOnly:
                builder.setPositiveButton("OK",listener);
                break;
            case OkCancel:
                builder.setPositiveButton("OK",listener);
                builder.setNegativeButton("Cancel",listener);
                break;
            case YesNo:
                builder.setPositiveButton("Yes",listener);
                builder.setNegativeButton("No",listener);
                break;
            }			
        }

        builder.create().show();
    }
*/
}