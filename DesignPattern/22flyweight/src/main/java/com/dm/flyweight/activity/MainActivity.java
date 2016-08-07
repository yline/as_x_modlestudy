package com.dm.flyweight.activity;

import android.os.Bundle;
import android.view.View;

import com.design.pattern.flyweight.R;
import com.dm.flyweight.ticket.Ticket;
import com.dm.flyweight.ticketfactory.TicketFactory;
import com.yline.base.BaseActivity;

public class MainActivity extends BaseActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		findViewById(R.id.btn_flyweight).setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				Ticket ticketA = TicketFactory.getTicket("北京", "青苔");
				ticketA.showTicketInfo("上铺");

				Ticket ticketB = TicketFactory.getTicket("北京", "青苔");
				ticketB.showTicketInfo("中铺");

				Ticket ticketC = TicketFactory.getTicket("北京", "杭州");
				ticketC.showTicketInfo("下铺");
			}
		});
	}

}
