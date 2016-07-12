package dm.bwl_note.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

import com.dm.memorandum.note.memoto.Memoto;

public class NoteEditText extends EditText
{
    
    public NoteEditText(Context context)
    {
        this(context, null);
    }
    
    public NoteEditText(Context context, AttributeSet attrs)
    {
        super(context, attrs); // 这里不能调用第三个,会出错的
    }
    
    public NoteEditText(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }
    
    /**
     * 为编辑器创建一个Memoto对象
     * @return
     */
    public Memoto createMemotoForEditText()
    {
        Memoto memoto = new Memoto();
        memoto.text = this.getText().toString();
        memoto.cursor = this.getSelectionStart();
        return memoto;
    }
    
    /**
     * 恢复编辑器状态
     * @param memoto
     */
    public void restoreEditText(Memoto memoto)
    {
        this.setText(memoto.text);
        this.setSelection(memoto.cursor);
    }
}
